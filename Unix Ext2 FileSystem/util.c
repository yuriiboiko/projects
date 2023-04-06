/*********** util.c file ****************/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <ext2fs/ext2_fs.h>
#include <string.h>
#include <libgen.h>
#include <sys/stat.h>
#include <time.h>


/**** globals defined in main.c file ****/
extern MINODE minode[NMINODE];
extern MINODE *root;
extern PROC   proc[NPROC], *running;

extern char gpath[128];
extern char *name[64];
extern int n;

extern int fd, dev;
extern int nblocks, ninodes, bmap, imap, iblk;

extern char line[128], cmd[32], pathname[128];

int get_block(int dev, int blk, char *buf)
{
   lseek(dev, (long)blk*BLKSIZE, 0);
   read(dev, buf, BLKSIZE);
}   

int put_block(int dev, int blk, char *buf)
{
   lseek(dev, (long)blk*BLKSIZE, 0);
   write(dev, buf, BLKSIZE);
}   

int tokenize(char *pathname)
{
  int i;
  char *s;
  printf("tokenize %s\n", pathname);

  strcpy(gpath, pathname);   // tokens are in global gpath[ ]
  n = 0;

  s = strtok(gpath, "/");
  while(s){
    name[n] = s;
    n++;
    s = strtok(0, "/");
  }
  name[n] = 0;
  
  for (i= 0; i<n; i++)
    printf("%s  ", name[i]);
  printf("\n");
}

// return minode pointer to loaded INODE
MINODE *iget(int dev, int ino)
{
  int i;
  MINODE *mip;
  char buf[BLKSIZE];
  int blk, offset;
  INODE *ip;

  for (i=0; i<NMINODE; i++){
    mip = &minode[i];
    if (mip->refCount && mip->dev == dev && mip->ino == ino){
       mip->refCount++;
       //printf("found [%d %d] as minode[%d] in core\n", dev, ino, i);
       return mip;
    }
  }
    
  for (i=0; i<NMINODE; i++){
    mip = &minode[i];
    if (mip->refCount == 0){
       //printf("allocating NEW minode[%d] for [%d %d]\n", i, dev, ino);
       mip->refCount = 1;
       mip->dev = dev;
       mip->ino = ino;

       // get INODE of ino to buf    
       blk    = (ino-1)/8 + iblk;
       offset = (ino-1) % 8;

       //printf("iget: ino=%d blk=%d offset=%d\n", ino, blk, offset);

       get_block(dev, blk, buf);
       ip = (INODE *)buf + offset;
       // copy INODE to mp->INODE
       mip->INODE = *ip;
       return mip;
    }
  }   
  printf("PANIC: no more free minodes\n");
  return 0;
}

void iput(MINODE *mip)
{
 int i, block, offset;
 char buf[BLKSIZE];
 INODE *ip;

 if (mip==0) 
     return;

 mip->refCount--;
 
 if (mip->refCount > 0) return;
 if (!mip->dirty)       return;
 
 /* write INODE back to disk */
 /**************** NOTE ******************************
  For mountroot, we never MODIFY any loaded INODE
                 so no need to write it back
  FOR LATER WROK: MUST write INODE back to disk if refCount==0 && DIRTY

  Write YOUR code here to write INODE back to disk
 *****************************************************/

   block = (mip->ino-1)/8 +iblk;
   offset = (mip->ino-1) % 8;

   get_block(mip->dev, block, buf);
   ip = (INODE *)buf;
   ip+=offset;
   *ip = mip->INODE;
   put_block(mip->dev, block, buf);
} 

int search(MINODE *mip, char *name)
{
   int i; 
   char *cp, c, sbuf[BLKSIZE], temp[256];
   DIR *dp;
   INODE *ip;

   printf("search for %s in MINODE = [%d, %d]\n", name,mip->dev,mip->ino);
   ip = &(mip->INODE);

   /*** search for name in mip's data blocks: ASSUME i_block[0] ONLY ***/

   get_block(dev, ip->i_block[0], sbuf);
   dp = (DIR *)sbuf;
   cp = sbuf;
   printf("  ino   rlen  nlen  name\n");

   while (cp < sbuf + BLKSIZE){
     strncpy(temp, dp->name, dp->name_len);
     temp[dp->name_len] = 0;
     printf("%4d  %4d  %4d    %s\n", 
           dp->inode, dp->rec_len, dp->name_len, dp->name);
     if (strcmp(temp, name)==0){
        printf("found %s : ino = %d\n", temp, dp->inode);
        return dp->inode;
     }
     cp += dp->rec_len;
     dp = (DIR *)cp;
   }
   return 0;
}

int getino(char *pathname)
{
  int i, ino, blk, offset;
  char buf[BLKSIZE];
  INODE *ip;
  MINODE *mip;

  printf("getino: pathname=%s\n", pathname);
  if (strcmp(pathname, "/")==0)
      return 2;
  
  // starting mip = root OR CWD
  if (pathname[0]=='/')
     mip = root;
  else
     mip = running->cwd;

  mip->refCount++;         // because we iput(mip) later
  
  tokenize(pathname);

  for (i=0; i<n; i++){
      printf("===========================================\n");
      printf("getino: i=%d name[%d]=%s\n", i, i, name[i]);
 
      ino = search(mip, name[i]);

      if (ino==0){
         iput(mip);
         printf("name %s does not exist\n", name[i]);
         return 0;
      }
      iput(mip);
      mip = iget(dev, ino);
   }

   iput(mip);
   return ino;
}

// search parent's data block for myino; SAME as search() but by myino
// copy its name STRING to myname
int findmyname(MINODE *parent, u32 myino, char *myname) 
{

   char *cp, buf[BLKSIZE], temp[256];
   DIR *dp;
   INODE *ip= &(parent->INODE);

   get_block(dev,ip->i_block[0],buf);
   
   dp = (DIR *)buf;
   cp = buf;

   while (cp < buf + BLKSIZE){
     if (dp->inode==myino){
         strncpy(myname, dp->name, dp->name_len);
         myname[dp->name_len]=0;
         return 0;
     }
     cp += dp->rec_len;
     dp = (DIR *)cp;
   }
   return -1;   

}

// mip points at a DIR minode
// WRITE your code here: myino = ino of .  return ino of ..
// all in i_block[0] of this DIR INODE.
//returns the ino of a parents directory
int findino(MINODE *mip, u32 *myino) // myino = i# of . return i# of ..
{
   char *buf[BLKSIZE];
   INODE *ip=&(mip->INODE);
   get_block(dev,ip->i_block[0],buf);

   DIR *dp=(DIR *)buf; //curent dir
   char *cp=buf;
   printf("%s ",dp->name);
   cp+=dp->rec_len;//move to next dir 
   dp = (DIR *)(cp);// parent dir
   printf("%s\n",dp->name);
   return dp->inode;//return its inode number
}


int tst_bit(char *buf, int bit){
   return buf[bit/8] & (1<<(bit%8));
}

int set_bit(char *buf, int bit){
   if(buf[bit/8] |= (1 <<(bit %8))){
      return 1;
   }
   return 0;
}

int clr_bit(char *buf, int bit){
   if(buf[bit/8] &= ~(1 << (bit%8))){
      return 1;
   }
   return 0;
}

//returns a FREE disk block number
int ialloc(int dev)  // allocate an inode number from inode_bitmap
{
  int  i;
  char buf[BLKSIZE];

  // read inode_bitmap block
  get_block(dev, imap, buf);

  for (i=0; i < ninodes; i++){
    if (tst_bit(buf, i)==0){
        set_bit(buf, i);
        put_block(dev, imap, buf);
        printf("allocated ino = %d\n", i+1); // bits count from 0; ino from 1
        return i+1;
    }
  }
  return 0;
}

//deallocate an inode (number)
int idalloc(int dev, int ino)
{
  int i;  
  char buf[BLKSIZE];

  if (ino > ninodes){  
    printf("inumber %d out of range\n", ino);
    return;
  }
   printf("Dealocating inode number: %d\n",ino);

  get_block(dev, imap, buf);  // get inode bitmap block into buf[]
  clr_bit(buf, ino-1);        // clear bit ino-1 to 0
  put_block(dev, imap, buf);  // write buf back
}

//deallocate an block (number)
int bdalloc(int dev, int bno)
{
  int i;  
  char buf[BLKSIZE];

  if (bno > nblocks){  
    printf("bnumber %d out of range\n", bno);
    return;
  }
   printf("Dealocating block number: %d\n",bno);

  get_block(dev, bmap, buf);  // get inode bitmap block into buf[]
  clr_bit(buf, bno-1);        // clear bit ino-1 to 0
  put_block(dev, bmap, buf);  // write buf back
}

int balloc(int dev){
   char buf[BLKSIZE];

   get_block(dev,bmap,buf);
   
   for(int i=0; i<nblocks; i++)
   {
      if(tst_bit(buf,i)==0){
         set_bit(buf, i);
         put_block(dev,bmap,buf);
         printf("Successfuly found a FREE disk block number %d\n", i+1);
         return i+1;
      }
   }
   printf("Unsuccessful finding a FREE disk block number \n");
   return 0;
}



int my_print(MINODE *mip)
{
   int i; 
   char *cp, c, sbuf[BLKSIZE], temp[256];
   DIR *dp;
   INODE *ip;

   printf("Ptinting MINODE = [%d, %d]\n",mip->dev,mip->ino);
   ip = &(mip->INODE);

   /*** search for name in mip's data blocks: ASSUME i_block[0] ONLY ***/

   get_block(dev, ip->i_block[0], sbuf);
   dp = (DIR *)sbuf;
   cp = sbuf;
   printf("  ino   rlen  nlen  name\n");

   while (cp < sbuf + BLKSIZE){
     strncpy(temp, dp->name, dp->name_len);
     temp[dp->name_len] = 0;
     printf("%4d  %4d  %4d    %s\n", 
           dp->inode, dp->rec_len, dp->name_len, dp->name);
     cp += dp->rec_len;
     dp = (DIR *)cp;
   }
   return 0;
}






