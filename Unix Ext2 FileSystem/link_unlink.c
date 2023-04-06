
/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;


int link(char *old_file, char *new_file){

    int oino = getino(old_file);
    if(oino==0){
        printf("ERROR: filename: %s doenst exist\n",old_file);
        return 0;
    }

    MINODE *omip=iget(dev,oino);
    if(S_ISDIR(omip->INODE.i_mode)){
        printf("ERROR: The file: %s is a DIR and symlink cannot be performed\n",old_file);
        return 0;
    }
    printf("%s ino is:%d \n",old_file, oino);
    printf("Verified that %s exists and is not a dir\n",old_file);


    char path1[128],path2[128];
    strcpy(path1,new_file);
    strcpy(path2,new_file);
    char *dname=dirname(path1);
    char *bname=basename(path2);
    printf("The dirname is %s\n",dname);
    printf("The basename is %s\n",bname);

    int npino = getino(dname);

    if(npino==0){
        printf("ERROR: dirname: %s doenst exist\n",dname);
        return 0;
    }
    MINODE *nmip=iget (dev,npino);

    if(!S_ISDIR(nmip->INODE.i_mode)){
        printf("ERROR: The dirname: %s is not DIR\n",dname);
        return 0;
    }

     //Checking if basename already exists
    if(search(nmip,bname)>0){
        printf("ERROR: basename: %s already exists.\n", bname);
        return 0;
    }

    printf("Verified that %s exists with ino: %d and file %s doesnt exist\n",dname, npino, bname);

    enter_name(nmip,oino, bname);
    printf("created %s file in %s dir with %d ino\n",bname,dname, oino);
    omip->INODE.i_links_count++;
    omip->dirty=1;
    iput(omip);
    iput(nmip);
    printf("cHECKING RESULTS OF LINK %s %s ...\n", old_file,new_file);
    search(nmip,bname);
    return 0;
}

int unlink(char *pathname){

    int ino=getino(pathname);
    if(ino==0){
        printf("ERROR: filename: %s doenst exist\n",pathname);
        return 0;
    }

    MINODE *mip=iget(dev,ino);

    if(S_ISDIR(mip->INODE.i_mode)){
        printf("ERROR: The file: %s is a DIR and unlink cannot be performed\n",pathname);
        return 0;
    }
    
    char path1[128], path2[128];
    strcpy(path1,pathname);    
    strcpy(path2,pathname);
    char *parent=dirname(path1);
    char *child=basename(path2);
    printf("The dirname is %s\n",parent);
    printf("The basename is %s\n",child);

    int pino = getino(parent);
    MINODE *pmip=iget(dev,pino);
    rm_child(pmip,child);
    pmip->dirty=1;
    iput(pmip);

    mip->INODE.i_links_count--;
    printf("Link count  is: %d !\n", mip->INODE.i_links_count);
    if(mip->INODE.i_links_count>0){
        mip->dirty=1;
    }else{
        for(int i=0; i<12; i++){

            if(mip->INODE.i_block[i]==0){
                printf("Dealocated no block number\n");
                break;
            }
            printf("Dealocating block number: %d\n", mip->INODE.i_block[i]);
            bdalloc(dev, mip->INODE.i_block[i]);
            mip->INODE.i_block[i]=0;
        }
        mip->INODE.i_size=0;
        mip->INODE.i_blocks=0;
        mip->dirty=1;
        idalloc(dev, mip->ino);
    }
    iput(mip);
    return 0;
}