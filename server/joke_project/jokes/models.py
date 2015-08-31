# -*- coding: UTF-8 -*-
from django.db import models
from datetime import datetime
import calendar

def timems2datetime(ms):
    return datetime.fromtimestamp(ms/1000.0)

def datetime2utctime(datetime):
    return long(calendar.timegm(datetime.utctimetuple())*1000.0 + datetime.microsecond /1000.0)

def fix_update_time():
    jokes = Joke.objects.all()
    for joke in jokes:
        joke.update_time = datetime2utctime(joke.pub_date)
        joke.save()

    comments = Comment.objects.all()
    for comment in comments:
        comment.update_time = datetime2utctime(comment.pub_date)
        comment.save()

    print 'fix end.joke count:%d,comments count:%d' % (len(jokes),len(comments))

class Author(models.Model):
    username = models.CharField(max_length= 100,primary_key=True)
    nickname = models.CharField(max_length= 100)
    password = models.CharField(max_length= 100,default="1234")
    group = models.IntegerField(default=0)
    createtime = models.DateTimeField('create time',default=datetime.now())
    avatar=models.CharField(max_length=1024,default="http://www.codeshome.com/")
    def __unicode__(self):
        return self.username; 
    pass

class Image(models.Model):
    path = models.CharField(max_length=1024)
    type = models.IntegerField(default = 0)
    size = models.IntegerField(default = 0)

    def __unicode__(self):
        return self.path

class Joke(models.Model):
    TYPE_NOPIC_JOKES = 0
    TYPE_PIC_JOKES = 1
    TYPE_LONG_JOKES = 2

    STATUS_HOLD = 0
    STATUS_APPROVED = 1
    STATUS_REJECTED = 2

    #joke类型定义
    JOKE_TYPES=(
        (TYPE_NOPIC_JOKES,'nopic_jokes'),  #无图片的段子
        (TYPE_PIC_JOKES,'pic_jokes'),    #带图片的段子
        (TYPE_LONG_JOKES,'long_jokes'),   #长段子，带图片
    )

    JOKE_STATUS=(
        (STATUS_HOLD,'hold'),    #未审核状态
        (STATUS_APPROVED,'approved'), #审核通过状态
        (STATUS_REJECTED,'rejected'), #审核拒绝状态
    )

    #来源
    SOURCE_SELF = 0
    SOURCE_QIUSHIBAIKE = 1
    JOKE_SOURCE={
        (SOURCE_SELF,'self created'),#自家
        (SOURCE_QIUSHIBAIKE,'糗事百科'),#糗事百科
    }

    type = models.IntegerField(default=0,choices=JOKE_TYPES)
    author = models.ForeignKey(Author)
    image = models.ForeignKey(Image,null=True,blank=True)
    content = models.CharField(max_length=102400)
    pub_date = models.DateTimeField('date published',default=datetime.now())
    update_time = models.BigIntegerField('date update',blank=True,default=0)
    status = models.IntegerField(default=0,choices=JOKE_STATUS)
    view_count = models.IntegerField(default=0)
    tag = models.CharField(max_length=1024,blank=True)
    #来源
    source = models.IntegerField(default=0,choices=JOKE_SOURCE)
    #外部ID,用于对爬虫导入的数据做去重
    outerid = models.CharField(max_length=1024,blank=True)

    def save(self,*args,**kwargs):
        ''' On save, update timestamps '''
        if not self.id:
            self.pub_date=datetime.now()
        self.update_time=datetime2utctime(datetime.now())

        return super(Joke,self).save(*args,**kwargs)


    def __unicode__(self):
        return self.content[0:12]

class DeviceInfo(models.Model):
    deviceid = models.CharField(max_length=100,primary_key=True,)
    model_name = models.CharField(max_length=100)
    platform = models.CharField(max_length= 20)
    osVersion = models.CharField(max_length=100)
    clientVesion = models.IntegerField(default=0)

    def __unicode__(self):
        return self.deviceid

class ThumbUp(models.Model):
    deviceid = models.CharField(max_length=100)
    joke = models.ForeignKey(Joke)

    def __unicode__(self):
        return self.deviceid + " thumbup " + self.joke.__unicode__()


class Like(models.Model):
    deviceid = models.CharField(max_length=100)
    joke = models.ForeignKey(Joke)

    def __unicode__(self):
        return self.deviceid + " like " + self.joke.__unicode__()

class Comment(models.Model):
    STATUS_INIT=0       #初始状态，待审核
    STATUS_APPROVED=1   #审核通过状态   
    STATUS_REJECTED=2   #审核拒绝状态

    COMMENT_STATUS=(
        (STATUS_INIT,"init"),
        (STATUS_APPROVED,"approved"),
        (STATUS_REJECTED,"rejected"),
    )
    deviceid = models.CharField(max_length=100)
    joke = models.ForeignKey(Joke)
    content = models.CharField(max_length=1024,blank=False)
    status = models.IntegerField(default=STATUS_INIT,choices=COMMENT_STATUS)
    pub_date=models.DateTimeField('date published',default=datetime.now())
    update_time=models.BigIntegerField('date update',default=0);

    def save(self,*args,**kwargs):
        ''' On save, update timestamps '''
        if not self.id:
            self.pub_date=datetime.now()

        self.update_time=datetime2utctime(datetime.now())
        return super(Comment,self).save(*args,**kwargs)

    def __unicode__(self):
        return self.deviceid + " comment " + self.joke.__unicode__() + ",content=" + self.content

pass


