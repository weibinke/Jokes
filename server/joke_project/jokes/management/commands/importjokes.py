#coding=utf-8
from django.shortcuts import render
from django.http import HttpResponse
from django.core import serializers
from jokes.models import Joke
from jokes.models import Author
from jokes.protocol.get_jokes import GetJokesRequest
from jokes.protocol.get_jokes import GetJokesResponse
from jokes.protocol.get_jokes import JokesItem
from jokes.protocol.get_jokes import BaseResponse
from jokes.protocol.get_jokes import CMD_DEFINE
from jokes.protocol.get_jokes import GetCommemtsResponse
from jokes.protocol.get_jokes import CommentItem
from jokes import models
from jokes.protocol import get_jokes
from django.db.models import Q
from jokes.models import Like
from jokes.models import Comment
from jokes.models import DeviceInfo
from jokes.models import ThumbUp
from jokes.models import Image
import datetime
import logging
import json
import xlrd
from django.core.management.base import BaseCommand
from optparse import make_option
import json
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )

#import Jokes from Json file

DEFALT_AUTHOR='default_author'
def readjson(filepath):
    f =open(filepath,'r')
    line = f.readline() 
    fdata=line
    while line:
        line=f.readline()
        fdata=fdata+line
    f.close()

    return fdata

class Command(BaseCommand):
    option_list = BaseCommand.option_list + (
    make_option('--filepath', '-l', dest='filepath', action='append',help='the file path'),)
    help = 'the file path'
    def handle(self, **options):
        print "import Jokes  start filepath = %s" % options.get('filepath')
        filePath=options.get('filepath')[0]
        fdata = readjson(filePath)
        
        datas = json.loads(fdata)

        joke_count=len(datas)
        print "import jokes rows=%d" % joke_count
        suc_count = 0
        image_count = 0
        image_added = 0
        add_count = 0
        for item in datas:
            try:
                imageurl = item.get('image')
                type=Joke.TYPE_NOPIC_JOKES
                image = None
                if len(imageurl):
                    image_count +=1
                    type=Joke.TYPE_PIC_JOKES
                    #处理Image，没有则创建
                    try:
                        image=Image.objects.get(path=imageurl)
                        print "image founded url=%s" % (imageurl)
                    except:
                        print "image not found add one url=%s" % (imageurl)
                        image=Image(path=imageurl)
                        image.save()
                        image_added +=1

                #创建一个默认的Author
                try:
                    author = Author.objects.get(username=DEFALT_AUTHOR)
                except:
                    print "Author not found add one username=%s" % (DEFALT_AUTHOR)
                    author = Author(username=DEFALT_AUTHOR,nickname=DEFALT_AUTHOR)
                    author.save()
                content = item.get('content')
                status = Joke.STATUS_APPROVED
                tag = item.get('tag')
                source = Joke.SOURCE_QIUSHIBAIKE
                outerid = item.get('id')
               
                try:
                    joke = Joke.objects.get(outerid=outerid,source=Joke.SOURCE_QIUSHIBAIKE)
                    print "Joke founded outerid=%s" % (outerid)
                except:
                    print "joke not found add one"
                    joke = Joke(type=type,author=author,image=image,content=content,status=status,tag=tag,source=source,outerid=outerid)
                    joke.save()
                
                    add_count +=1
                suc_count +=1
                print "Joke item end  content=%s" % (content)
            except:
                print "parse item failed"
                raise

            print "parse item end.outerid=%s" % (outerid)
        print "import jokes completed total count=%d,success count=%d,added count=%d,image count=%d,image added=%d" % (joke_count,suc_count,add_count,image_count,image_added)
