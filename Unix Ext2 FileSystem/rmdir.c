/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;

int rmdir(char *pathname){
    if(pathname[0]==0){
        printf("ERROR: no filename provided\n");
        return 0;
    }
    int ino=getino(pathname);

    if(ino==0){
        printf("ERROR: dirname: %s doenst exist\n",pathname);
        return 0;
    }
    MINODE *mip=iget (dev,ino);


    if(!S_ISDIR(mip->INODE.i_mode)){
        printf("ERROR: The dir: %s is not a DIR and command rmdir cannot be performed\n", pathname);
        return 0;
    }

    if(mip->refCount>2){
        printf("ERROR: The dir: %s is BUSY and command rmdir cannot be performed\n", pathname);
        printf("refCount is %d\n", mip->refCount);

        return 0;
    }

    if(is_empty(mip)==0){
        printf("ERROR: The dir: %s is not empty and command rmdir cannot be performed\n", pathname);
        return 0;      
    }

    int pino=findino(mip,ino);
    printf("Parent ino =%d\n",pino);
    MINODE *pmip=iget (mip->dev,pino);


    char *name;
    findmyname(pmip,ino,name);
    printf("Name of dir being removed is %s\n",name);


    rm_child(pmip,name);
    pmip->INODE.i_links_count--;
    pmip->dirty=1;
    iput(pmip);

    for(int i=0;i<12;i++){
        if(mip->INODE.i_block[i]!=0){
            bdalloc(mip->dev,mip->INODE.i_block[i]);
        }
    }
    idalloc(mip->dev, mip->ino);
    mip->dirty=1;
    iput(mip);
    return 0;
}




int is_empty(MINODE *mip){

   
    char buf[BLKSIZE], name[128];
    DIR *dp;
    char *cp;
    INODE *ip=&mip->INODE;
    if(ip->i_links_count>2){
        printf("Dir is not empty\n");
        return 0;
    }
    get_block(dev,ip->i_block[0],buf);   
    if(ip->i_block[0]==0){
        printf("Dir is  empty\n");
        return 1; //empty
    }  
     dp=(DIR *)buf;
    cp=buf;
    while(cp<buf+BLKSIZE){

        strncpy(name,dp->name,dp->name_len);
        name[dp->name_len]=0;
        if(strcmp(name,".")&& strcmp(name,"..")){
            printf("Dir is not empty\n");
            return 0;//not empy more than two dirs 
        }
        cp+=dp->rec_len;
        dp=(DIR *)cp;
    }
    printf("Dir is  empty\n");
    return 1; //empty
}






int rm_child(MINODE *pmip, char *name){
    printf("INSIDE rm_child\n");
    printf("Parent ino: %d\n",pmip->ino);
    printf("Removing name: %s\n", name);

    char buf[BLKSIZE],tmp[256];
    DIR *dp, *pdp, *edp;
    char *cp, *ecp; 
    INODE *pip=&pmip->INODE;

    for(int i=0; i<12; i++){
        printf("i_block: %d\n",pip->i_block[i]);

        if(pip->i_block[i]==0){
            break;
        }

        get_block(pmip->dev,pip->i_block[i],buf);
        dp=(DIR *)buf;
        pdp=dp;
        cp=buf;
        while(cp<buf+BLKSIZE){

           strncpy(tmp,dp->name,dp->name_len);
           tmp[dp->name_len]=0;

           if(!strcmp(tmp,name)){
               if((cp+dp->rec_len==buf+BLKSIZE)&(cp==buf)){//case 1 first and only entry
                   bdalloc(dev,pip->i_block[i]);
                    pip->i_size-=BLKSIZE;
                    i++;
                    while(i<12){
                        if(pip->i_block[i]==0){
                            break;
                        }
                        get_block(dev,pip->i_block[i],buf);
                        put_block(dev, pip->i_block[i-1],buf);
                        i++;
                    }
                    pmip->dirty=1;
                    iput(pmip);
                    return 0;
               }else if(cp+dp->rec_len==buf+BLKSIZE){//case two last one int the buf 
                    printf("previousdp name: %s\n",pdp->name);
                    pdp->rec_len+=dp->rec_len;
                    put_block(dev,pip->i_block[i],buf);
                    pmip->dirty=1;
                    iput(pmip);
                    return 0;
               }else{//case 3 in the middle of buf
                   edp=dp;
                   ecp=cp;
                   while(ecp<buf+BLKSIZE){
                       ecp+=edp->rec_len;
                       edp=(DIR *)ecp;
                   }
                    edp->rec_len+=dp->rec_len;
                    cp+= dp->rec_len;
                    memcpy(dp, cp,((buf+BLKSIZE)-cp));
                    put_block(dev,pip->i_block[i],buf);
                    pmip->dirty=1;
                    iput(pmip);
                    return 0;
               }
           } 
            cp+= dp->rec_len;
            pdp=dp;
            dp=(DIR *)cp;
        }
    }
    return 0;
}