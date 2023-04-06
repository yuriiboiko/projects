/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;


int my_mkdir(char *pathname){

if(pathname[0]==0){
        printf("ERROR: no filename provided\n");
        return 0;
    }
    else if(pathname[0]=='/'){
        dev=root->dev;
        printf("DEV=%d\n",dev);
    }else{
        dev = running->cwd->dev;
        printf("DEV=%d\n",dev);

    }

    char path1[128],path2[128];
    strcpy(path1,pathname);
    strcpy(path2,pathname);
    char *dname=dirname(path1);
    char *bname=basename(path2);
    printf("The dirname is %s\n",dname);
    printf("The basename is %s\n",bname);


    int pino=getino(dname);
    if(pino==0){
        printf("ERROR: dirname: %s doenst exist\n",dname);
        return 0;
    }
    MINODE *pmip=iget (dev,pino);
    if(!S_ISDIR(pmip->INODE.i_mode)){
        printf("ERROR: The dirname: %s is not DIR\n",dname);
        return 0;
    }

    //Checking if basename already exists
    if(search(pmip,bname)>0){
        printf("ERROR: basename: %s already exists.\n", bname);
        return 0;
    }

    kmkdir(pmip, bname);

    pmip->INODE.i_links_count++;
    pmip->dirty=1;
    iput(pmip);

    return 0;
}


int kmkdir(MINODE *pmip, char *name){
    int ino=ialloc(dev);
    int bno=balloc(dev);

    printf("kmkdir allocated ino: %d, bno: %d\n",ino,bno);

    MINODE *mip=iget(dev,ino);

    INODE *ip = &mip->INODE;
    
    ip->i_mtime = time(0L);
    ip->i_atime = time(0L);
    ip->i_ctime = time(0L);
    ip->i_mode = 0x41ED; // 0x41ED: Dir type and permissions
    ip->i_uid = running->uid; // owner uid
    ip->i_gid = running->gid; // group Id
    ip->i_size = BLKSIZE; // size in bytes
    ip->i_links_count = 2; // links count=2 because . and..
    printf("time is: %ld\n",ip->i_mtime);

    ip->i_blocks = 2; // LINUX: Blocks count in 512 byte chunks
    ip->i_block[0] = bno;
    for(int i=1;i<15;i++){
        ip->i_block[0] = 0;
    }
    mip->dirty = 1; // mark minode dirty
    iput(mip); // write INODE to disk

    char *buf[BLKSIZE], *cp;
    DIR *dp;

    get_block(dev,bno,buf);
    dp=(DIR *)buf;
    cp=buf;

    dp->inode=ino;
    dp->rec_len=12;
    dp->name_len=1;
    dp->name[0]='.';
    
    cp+=12;
    dp=(DIR *)cp;

    dp->inode=pmip->ino;
    dp->rec_len=BLKSIZE-12;
    dp->name_len=2;
    dp->name[0]='.';
    dp->name[1]='.';
    
    put_block(dev,bno,buf);

    enter_name(pmip, ino, name);

    return 0;
}



int my_creat(char *pathname){
    //printf("Inside create function\n");
    if(pathname[0]==0){
        printf("ERROR: no filename provided\n");
        return 0;
    }
    else if(pathname[0]=='/'){
        dev=root->dev;
    }else{
        dev = running->cwd->dev;
    }

    char path1[128],path2[128];
    strcpy(path1,pathname);
    strcpy(path2,pathname);
    char *dname=dirname(path1);
    char *bname=basename(path2);
    printf("The dirname is %s\n",dname);
    printf("The basename is %s\n",bname);

    //Cheking if dir name exists and is a DIR
    int pino=getino(dname);
    if(pino==0){
        printf("ERROR: dirname: %s doenst exist\n",dname);
        return 0;
    }
    MINODE *pmip=iget (dev,pino);
    if(!S_ISDIR(pmip->INODE.i_mode)){
        printf("ERROR: The dirname: %s is not DIR\n",dname);
        return 0;
    }

    //Checking if basename already exists
    if(search(pmip,bname)>0){
        printf("ERROR: basename: %s already exists.\n", bname);
        return 0;
    }
    creat_file(pmip,bname);
    pmip->dirty=1;
    iput(pmip);
    return 0;
}

int creat_file(MINODE *pmip, char *name){
    printf("Inside create_file functions creating a file name %s\n",name);

    int ino = ialloc(dev);
    //int bno = balloc(dev);

    printf("ino: %d, bno: Not needed for file creation\n",ino);

    MINODE *mip = iget(dev, ino);

    INODE *ip = &mip->INODE;
    
    ip->i_mtime = time(0L);
    ip->i_atime = time(0L);
    ip->i_ctime = time(0L);
    ip->i_mode = 0x81A4; // 0x81A4: File type and permissions
    ip->i_uid = running->uid; // owner uid
    ip->i_gid = running->gid; // group Id
    ip->i_size = BLKSIZE; // size in bytes
    ip->i_links_count = 1; // links count=1 
    printf("time is: %ld\n",ip->i_mtime);

    ip->i_blocks = 2; // LINUX: Blocks count in 512 byte chunks
    ip->i_block[0] = 0;
    mip->dirty = 1; // mark minode dirty
    iput(mip); // write INODE to disk

    enter_name(pmip, ino, name);
    printf("Verifying that parent contains the file...\n");
    search(pmip,name);
    return 0;
}




int enter_name(MINODE *pmip, int ino, char *name){
    
    printf("Inside enter_name functions creating a file name %s\n",name);

    INODE *ip=&pmip->INODE;
    char buf[BLKSIZE];
    DIR *dp;
    char *cp;    
    char temp[256];
    for(int i=0; i < 12 ;i++){
        if(ip->i_block[i]==0){
            break;
        }
        get_block(pmip->dev,ip->i_block[i], buf);
        dp=(DIR *)buf;
        cp=buf;

        printf("Getting parent data block...\n RESULTS of buf:\n %.*s",BLKSIZE,buf);

        while(cp+dp->rec_len <buf+BLKSIZE){
            strncpy(temp, dp->name, dp->name_len);
            temp[dp->name_len] = 0;
            printf("%4d  %4d  %4d    %s\n", dp->inode, dp->rec_len, dp->name_len, dp->name);
            cp+= dp->rec_len;
            dp=(DIR *)cp;

        }
        strncpy(temp, dp->name, dp->name_len);
        temp[dp->name_len] = 0;
        printf("%4d  %4d  %4d    %s\n", dp->inode, dp->rec_len, dp->name_len, dp->name);
            
        int need_len = 4*((8+strlen(name) + 3)/4);
        int ideal_len = 4*((8+dp->name_len + 3)/4);
        int remainder = dp->rec_len - ideal_len;

        printf("Needed length for %s is %d\n", name, need_len);
        printf("Ideal length if %s is %d\n", dp->name, ideal_len);
        printf("Remainder length is %d\n",  remainder);


        if(remainder >= need_len){
            dp->rec_len=ideal_len;
            cp+=ideal_len;
            dp=(DIR *)cp;

            strcpy(dp->name,name);
            dp->inode=ino;
            dp->name_len=strlen(name);
            dp->rec_len=remainder;
            put_block(dev, ip->i_block[i], buf);
            return 0;
        }
        else{
            int bno = balloc(pmip->dev);
            ip->i_size = BLKSIZE;
            ip->i_block[i]=bno;

            get_block(pmip->dev, ip->i_block[i], buf);
            dp=(DIR *)buf;
            
            strcpy(dp->name,name);
            dp->inode=ino;
            dp->name_len=strlen(name);
            dp->rec_len=remainder;
            put_block(pmip->dev, ip->i_block[i], buf);
            return 0;
        }

    }

    return 0;
}