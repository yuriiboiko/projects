
/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;


int symlink(char *old_file, char *new_file){

    int oino = getino(old_file);
    if(oino==0){
        printf("ERROR: filename: %s doenst exist\n",old_file);
        return 0;
    }

    MINODE *omip=iget(dev,oino);

    printf("Dir  name %s exists with %d ino\n",old_file,oino);

    char path1[128],path2[128];
    strcpy(path1,new_file);
    strcpy(path2,new_file);
    char *parent=dirname(path1);
    char *child=basename(path2);
    printf("The dirname is %s\n",parent);
    printf("The basename is %s\n",child);

    int npino = getino(parent);

    if(npino==0){
        printf("ERROR: dirname: %s doenst exist\n",parent);
        return 0;
    }
    MINODE *npmip=iget (dev,npino);

    if(!S_ISDIR(npmip->INODE.i_mode)){
        printf("ERROR: The dirname: %s is not DIR\n",parent);
        return 0;
    }

     //Checking if basename already exists
    if(search(npmip,child)>0){
        printf("ERROR: basename: %s already exists.\n", child);
        return 0;
    }

    creat_file(npmip, child);
    
    my_print(npmip);
    
    int ncino = getino(child);

    if(ncino==0){
        printf("ERROR: file: %s creationg wasn't successful.\n", child);
        return 0;
    }

    MINODE *ncmip=iget (dev,ncino);
    ncmip->INODE.i_mode=0xA1FF;

    char *oldname=basename(old_file);
    strcpy((char*)ncmip->INODE.i_block,oldname);

    ncmip->INODE.i_size=strlen(oldname);
    ncmip->dirty=1;
    iput(ncmip);

    npmip->dirty=1;
    iput(npmip);
    return 0;
}

