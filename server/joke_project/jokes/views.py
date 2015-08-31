#coding=utf-8
from django.shortcuts import render
from django.http import HttpResponse
from django.core import serializers
from jokes.models import Joke
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
import datetime
import logging

import json

def index(request):
    return HttpResponse("This is home!")

logger=logging.getLogger("jokes.views")

# save device info
def save_device_info(request):
    req_dict=request.GET
    deviceid=req_dict.get('deviceid')
    version=int(req_dict.get('cv',0))
    model = req_dict.get('model',"")
    os = req_dict.get('os',"")
    platform = req_dict.get('platform',"")

    if deviceid != "":
        device=DeviceInfo(deviceid=deviceid,model_name=model,platform=platform,osVersion=os,clientVesion=version)
        device.save()


'''
    Get jokes list by type and startpos,count
'''
def get_jokes_list(request):
#    jokes = Joke.objects.all()
#    data = serializers.serialize('json',[jokes])[1:-1]
#   data = serializers.serialize('json',jokes)  
   
    logger.info('get_jokes_list start')
    req_dict=request.GET
    if request.method == 'POST':
        post_data=str(request.body)
        return HttpResponse(post_data)
    
    is_head = req_dict.get('ishead',1)
    start=req_dict.get('start',0)
    count=req_dict.get('count',0)
    type=req_dict.get('type',0)
    deviceid=req_dict.get('deviceid',"")

#    data=get_jokes.test()
    joke_result=[]
    number = "1"
    logger.info("get_jokes_list is_head=%d" % int(is_head))
    starttime=long(start)
    if is_head == number:
        jokes_result=Joke.objects.order_by("-update_time").filter(Q(type=type),Q(update_time__gt=starttime),Q(status=Joke.STATUS_APPROVED))[0:count]    
    else:
        jokes_result=Joke.objects.order_by("-update_time").filter(Q(type=type),Q(update_time__lt=starttime),Q(status=Joke.STATUS_APPROVED))[0:count]    

    items=[]
    for joke in jokes_result:
        #获取评论数和赞数
        comment_count=len(Comment.objects.filter(Q(joke=joke)))
        thumbup_count=len(ThumbUp.objects.filter(Q(joke=joke)))
        
        '''
        #增加viewcount
        joke.view_count=joke.view_count+1
        joke.save()
        '''
        
        is_favorite = False
        try:
            like = Like.objects.filter(deviceid=deviceid,joke=joke)[:1]
            if len(like):
                is_favorite=True

        except:
            is_favorite = False
        
        is_thumbup = False
        
        try:
            thumbup = ThumbUp.objects.get(deviceid=deviceid,joke=joke)
            if thumbup:
                is_thumbup = True
        except:
            is_thumbup = False
        ts=models.datetime2utctime(joke.pub_date)
        ts_update=joke.update_time
        imageurl = ''
        if joke.image:
            imageurl = joke.image.path
        item=JokesItem(id=joke.id,type=joke.type,author=joke.author.username,avatar=joke.author.avatar,content=joke.content,imageurl=imageurl,time=ts,update_time=ts_update,thumb_up_count=thumbup_count,comment_count=comment_count,is_favorite=is_favorite,is_thumbup=is_thumbup)
        items.append(item)
    
    resp=GetJokesResponse(ret=CMD_DEFINE.RET_OK,count=len(jokes_result),start=start,next_pos=0,items=items)
    data=get_jokes.object2Json(resp)
    #保存deviceinfo
    save_device_info(request)

    logger.info("get_jokes_list end")
    return HttpResponse(data)
'''
    赞
'''
def thumbup(request):
    req_dict = request.GET
    deviceid=req_dict.get("deviceid","")
    is_cancel=req_dict.get("is_cancel",0)

    ret=CMD_DEFINE.RET_OK
    try:
        joke_id=req_dict.get("joke_id",0)
        joke=Joke.objects.get(id=joke_id)
        if is_cancel!="0":
            logger.info("thumbup is_cancel=%s" % is_cancel)
            try:
                thumbup=ThumbUp.objects.get(deviceid=deviceid,joke=joke)
                thumbup.delete()
            except:
                logger.info("thumbup get thumbup failed")
                pass 
        else :
            thumbup=ThumbUp(deviceid=deviceid,joke=joke)
            thumbup.save()
        
    except:
        ret=CMD_DEFINE.RET_JOKE_ID_NOTFOUND
    
    resp=BaseResponse(ret=ret)
    data=get_jokes.object2Json(resp)
    
    #保存deviceinfo
    save_device_info(request)

    return HttpResponse(data)

'''
    收藏
'''
def like(request):
    logger.debug('like request=%s' % request)
    req_dict = request.GET
    deviceid=req_dict.get("deviceid","")
    ret=CMD_DEFINE.RET_OK
    is_cancel=req_dict.get("is_cancel",0)
    try:
        joke_id=req_dict.get("joke_id",0)
        joke=Joke.objects.get(id=joke_id)

        if is_cancel != "0":
            try:
                result=Like.objects.filter(deviceid=deviceid,joke=joke).delete()
                logger.info('like delete result=%s' % result)
            except:
                pass
        else:
            like=Like(deviceid=deviceid,joke=joke)
            like.save()

    except:
        ret=CMD_DEFINE.RET_JOKE_ID_NOTFOUND

    resp=BaseResponse(ret=ret)
    data=get_jokes.object2Json(resp)

    #保存deviceinfo
    save_device_info(request)

    return HttpResponse(data)

'''
    评论
'''
def comment(request):
    req_dict = request.GET
    deviceid=req_dict.get("deviceid","")
    content=req_dict.get("content","")
    
    ret=CMD_DEFINE.RET_ERROR
    try:
        if content:
            joke_id=req_dict.get("joke_id",0)
            joke=Joke.objects.get(id=joke_id)
            comment=Comment(deviceid=deviceid,joke=joke,content=content,status=Comment.STATUS_INIT,pub_date=datetime.datetime.now())
            comment.save()
            ret=CMD_DEFINE.RET_OK
        else:
            ret=CMD_DEFINE.RET_INVAILED_ARG
    except Exception,e:
        ret=CMD_DEFINE.RET_JOKE_ID_NOTFOUND
        logger.error(e)

    resp=BaseResponse(ret=ret)
    data=get_jokes.object2Json(resp)

    #保存deviceinfo
    save_device_info(request)

    return HttpResponse(data)

'''
    Get comment list
'''
def get_comments(request):
    req_dict=request.GET
    
    joke_id=req_dict.get("joke_id",0)
    start=req_dict.get('start',0)
    count=req_dict.get('count',0)
    deviceid=req_dict.get('deviceid',"")

    ret=CMD_DEFINE.RET_ERROR   
    items=[]
    next_pos=0
    try:
        joke=Joke.objects.get(id=joke_id)
        starttime=long(start)
        comments=[]
        if long(start) > 0:
            comments=Comment.objects.order_by("-update_time").filter(joke=joke,update_time__lt=starttime)[:count]
        else:
            comments=Comment.objects.order_by("-update_time").filter(joke=joke,update_time__gt=starttime)[:count]
        for item in comments:
            ts=item.update_time
            comment_item=CommentItem(id=item.id,author=CommentItem.DEFAULT_AUTHOR,content=item.content,joke_id=joke_id,status=item.status,pub_date=ts)
            items.append(comment_item)
            if item.id > next_pos:
                next_pos=item.id 
        ret=CMD_DEFINE.RET_OK
    except:
        ret=CMD_DEFINE.RET_JOKE_ID_NOTFOUND

    get_comments_resp=GetCommemtsResponse(ret=ret,count=len(items),next_pos=next_pos,items=items)
    
    data=get_jokes.object2Json(get_comments_resp)    

    save_device_info(request)

    return HttpResponse(data)

def get_joke(request):
    req_dict=request.GET
    joke_id=req_dict.get("joke_id",0)
    deviceid=req_dict.get('deviceid','');
    ret=CMD_DEFINE.RET_ERROR   

    jokes_result=[]
    try:
        joke=Joke.objects.get(id=joke_id)
        jokes_result.append(joke)

        ret=CMD_DEFINE.RET_OK
    except:
        ret=CMD_DEFINE.RET_JOKE_ID_NOTFOUND

    items=[]
    for joke in jokes_result:
        #获取评论数和赞数
        comment_count=len(Comment.objects.filter(Q(joke=joke)))
        thumbup_count=len(ThumbUp.objects.filter(Q(joke=joke)))
        
        '''
        #增加viewcount
        joke.view_count=joke.view_count+1
        joke.save()
        '''
        
        is_favorite = False

        try:
            like = Like.objects.filter(deviceid=deviceid,joke=joke)[:1]
            logger.info('get_joke,like len:%d' % len(like))
            if len(like):
                is_favorite=True

        except Exception,e:
            is_favorite = False
        
        is_thumbup = False
        
        try:
            thumbup = ThumbUp.objects.get(deviceid=deviceid,joke=joke)
            if thumbup:
                is_thumbup = True
        except:
            is_thumbup = False
        ts=models.datetime2utctime(joke.pub_date)
        ts_update=joke.update_time
        imageurl=''
        if joke.image:
            imageurl = joke.image.path
        item=JokesItem(id=joke.id,type=joke.type,author=joke.author.username,avatar=joke.author.avatar,content=joke.content,imageurl=imageurl,time=ts,update_time=ts_update,thumb_up_count=thumbup_count,comment_count=comment_count,is_favorite=is_favorite,is_thumbup=is_thumbup)
        items.append(item)
    
    resp=GetJokesResponse(ret=CMD_DEFINE.RET_OK,count=len(jokes_result),items=items)
    data=get_jokes.object2Json(resp)
 
    save_device_info(request)
    
    return HttpResponse(data);
 
'''
    审核通过，发布信息
'''
def publish(request):
    save_device_info(request)
    return HttpResponse("publish")

'''
    信息下架
'''
def unpublish(request):
    save_device_info(request)
    return HttpResponse("unpublish")
