#coding=utf-8

import json
'''
Get_jokes协议类
'''
class CMD_DEFINE:
    RET_OK=0
    RET_ERROR=-1
    RET_INVAILED_ARG=-2
    RET_JOKE_ID_NOTFOUND=-3
    RET_FORBIDDEN=-4

def object2Json(object):
    return json.dumps(object,indent=4,sort_keys=False,separators=(',',':'),default=lambda obj:obj.__dict__)

class BaseRequest(object):
    def __init__(self,deviceid=""):
        self.deviceid=deviceid
    
    pass

class BaseResponse(object):
    def __init__(self,ret=0):
        self.ret=ret
    
    pass

class JokesItem(object):
    def __init__(self,id=0,type=0,author="",avatar="",content="",imageurl="",time=0,update_time=0,thumb_up_count=0,comment_count=0,is_favorite=False,is_thumbup=False):
        self.id = id
        self.type = type
        self.author=author
        self.avatar=avatar
        self.content=content
        self.imageurl=imageurl
        self.time=time
        self.update_time=update_time
        self.thumb_up_count=thumb_up_count
        self.comment_count=comment_count
        self.is_favorite=is_favorite
        self.is_thumbup=is_thumbup
    pass
    
    @staticmethod
    def dict2JokeItems(d):
        item=JokesItem()
        item.type=d.get('type',0)
        item.author=d.get('author',"")
        item.avatar=d.get('avatar',"")
        item.content=d.get('content',"")
        item.imageurl=d.get('imageurl',"")
        item.time=d.get('time',0)
        item.thumb_up_count=d.get('thumb_up_count',0)
        item.comment_count=d.get('comment_count',0)

        return item
    

class GetJokesRequest(BaseRequest):
    def __init__(self,type=0,deviceid="",start=0,count=0):
        BaseRequest.__init__(self,deviceid=deviceid)    
        self.type = type
        self.start = start
        self.count = count

    pass

def json2GetJokesRequest(data):
    req = GetJokesRequest()
    d=json.loads(data)
    req.type=d.get("type",0)
    req.deviceid=d.get("deviceid","")
    req.start=d.get("start",0)
    req.count=d.get("count",0)
    return req

pass

class GetJokesResponse(BaseResponse):
    def __init__(self,ret=0,count=0,start=0,next_pos=0,items=[]):
        BaseResponse.__init__(self,ret=ret)
        self.count=count
        self.start=start
        self.next_pos=next_pos
        self.items=items
    pass

    @staticmethod
    def json2GetJokesResponse(data):
        d=json.loads(data)
        resp=GetJokesResponse()
        resp.ret=d.get("ret",0)
        resp.count=d.get("count",0)
        resp.start=d.get("start",0)
        resp.next_pos = d.get("next_pos",0)
        dict_items=d.get('items',[])
        resp.items=[]
        for item in dict_items:
            resp.items.append(JokesItem.dict2JokeItems(item))

        return resp

class CommentItem(object):
    DEFAULT_AUTHOR=""
    def __init__(self,id=0,author=DEFAULT_AUTHOR,content="",joke_id=0,status=0,pub_date=0):
        self.id=id
        self.author=author
        self.content=content
        self.joke_id=joke_id
        self.status=status
        self.pub_date=pub_date

class GetCommemtsResponse(BaseResponse):
    def __init__(self,ret=0,count=0,start=0,next_pos=0,items=[]):
        BaseResponse.__init__(self,ret=ret)
        self.count=count
        self.start=start
        self.next_pos=next_pos
        self.items=items

def test():
    joke1 = JokesItem(type=11,author="wang",content="这是内容")
    joke2 = JokesItem(type=12,author="li",content="这是内容2")

    items=[joke1,joke2]

    request = GetJokesRequest(deviceid="1xcgsdfsdsa")

    data=json.dumps(request,default=lambda obj:obj.__dict__)
    request=json2GetJokesRequest(data)
    data=json.dumps(request,default=lambda obj:obj.__dict__)

#    data='[{"content": "\u8fd9\u662f\u5185\u5bb9", "type": 11, "author": "wang"}, {"content": "\u8fd9\u662f\u5185\u5bb92", "type": 12, "author": "li"}]'
#   items=json.loads(data,object_hook=get_jokes.dict2JokeItems)

    #data='{"count": 0, "start": 0, "type": 0, "deviceid": "1232sdsa"}'
#    request=json.loads(data,object_hook=get_jokes.dict2GetJokesRequest)

#    data=json.dumps(request.items,default=lambda obj:obj.__dict__)

   #data=json.dumps(items,default=lambda obj:obj.__dict__)
    resp=GetJokesResponse(count=133,start=13,next_pos=23,items=items)
    data=object2Json(resp)
    resp=GetJokesResponse.json2GetJokesResponse(data)
    resp.count=12222
    data=object2Json(resp)
    return data


