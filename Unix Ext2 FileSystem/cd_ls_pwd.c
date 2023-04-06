
char *t1 = "xwrxwrxwr-------";
char *t2 = "----------------";

/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;

/************* cd_ls_pwd.c file **************/
int cd(char *pathname)
{
  // READ Chapter 11.7.3 HOW TO chdir
  if(!strcmp(pathname,""))
    return 0;
  int ino = getino(pathname);
  if(ino==0){
    printf("The pathname = %s doesnt exist",pathname);
    return -1;
  }
  MINODE *mip = iget(dev,ino);
  u16 mode =mip->INODE.i_mode;
  if(!S_ISDIR(mode)){
    printf("ERROR: Not a dir\n");
    return -1;
  }
  iput(running->cwd);
  running->cwd=mip;
  return 1;
}

int ls_file(MINODE *mip, char *name)
{
  char time[256];
  u16 mode =mip->INODE.i_mode;

  if(S_ISREG(mode)){
    printf("%c", '-');
  }
  if(S_ISDIR(mode)){
    printf("%c", 'd');
  }if(S_ISLNK(mode)){
    printf("%c", 'l');
  }

  for(int i = 8;i >= 0;i--){
    if(mode & (1 << i)){
      printf("%c",t1[i]);
    }else{
      printf("%c",t2[i]);
    }
  }

  printf(" %d",mip->INODE.i_links_count);
  printf(" %d",mip->INODE.i_gid);
  printf(" %d",mip->INODE.i_uid);
  printf(" %d",mip->INODE.i_size);

  if(asctime(gmtime(&(mip->INODE.i_mtime)))==NULL){
    printf(" %s", asctime(gmtime(&(mip->INODE.i_mtime))));
  }else{
    strcpy(time, ctime((time_t *)&(mip->INODE.i_mtime)));

    time[strlen(time)-1]=0;

    printf(" %s ",time);
    //printf(" %s", asctime(gmtime(&(mip->INODE.i_mtime))));
  }

  //printf("\n %ld \n",mip->INODE.i_mtime);

  //strcpy(time, ctime((time_t *)&(mip->INODE.i_mtime)));

  //time[strlen(time)-1]=0;

  //printf(" %s ",time);
  //printf(" %s", asctime(gmtime(&(mip->INODE.i_mtime))));
  printf(" %s",name);

  if(S_ISLNK(mode)){
    printf("->%s", (char *)mip->INODE.i_block);
  }
  printf(" [%d, %d]",mip->dev,mip->ino);
  printf("\n");
  //printf("ls_file: to be done: READ textbook!!!!\n");
  // READ Chapter 11.7.3 HOW TO ls
  return 0;
}

int ls_dir(MINODE *mip)
{
  //printf("ls_dir: list CWD's file names; YOU FINISH IT as ls -l\n");

  char buf[BLKSIZE], entityname[256], time[64];
  DIR *dp;
  char *cp;
  u16 mode = mip->INODE.i_mode;
  MINODE *pmip;

  get_block(dev, mip->INODE.i_block[0], buf);
  dp = (DIR *)buf;
  cp = buf;
  while(cp<buf +BLKSIZE){
    strncpy(entityname, dp->name, dp->name_len);
    entityname[dp->name_len] = 0;
    pmip=iget(dev,dp->inode);
    ls_file(pmip,entityname);
    cp+=dp->rec_len;
    dp= (DIR *)cp;
  }
  return 0;
}

int ls(char *pathname)
{
  if(!strcmp(pathname,"")){
    ls_dir(running->cwd);
  }else {
    int ino=getino(pathname);
    if(ino==-1){
      printf("pathname = %s doesnt exist",pathname);
      return -1;
    }
    MINODE*mip = iget(dev,ino);
    int mode = mip->INODE.i_mode;
    if(S_ISDIR(mode)){
      ls_dir(mip);
    }else{
      char *fname = basename(pathname);
      ls_file(mip,fname);
    }
  }
}

char *pwd(MINODE *wd)
{
  //printf("pwd: READ HOW TO pwd in textbook!!!!\n");
  printf("pwd=");

  if (wd == root){
    printf("/\n");
    return;
  }else{
    rpwd(wd);
    printf("\n");
  }
}

void rpwd(MINODE *wd){
  
  if(wd==root){
    return;
  }
  int ino, pino;
  char buf[BLKSIZE], name[256];

  ino=wd->ino;
  pino = findino(wd,&ino); //

  MINODE *pip = iget(dev,pino);

  findmyname(pip,ino,name);
  rpwd(pip);
  printf("/%s",name);
  return;
}



