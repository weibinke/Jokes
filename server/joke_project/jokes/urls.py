# -*- coding: utf-8 -*-  
from django.conf.urls import patterns,url

from jokes import views

urlpatterns = patterns('',
    url(r'^$',views.index,name='index'),
    #获取Joke列表
    url(r'^get_jokes_list/$',views.get_jokes_list,name='get_jokes_list'),
    #点赞
    url(r'^thumbup/$',views.thumbup,name='thumbup'),
    #收藏
    url(r'^like/$',views.like,name='like'),
    #评论
    url(r'^comment/$',views.comment,name='comment'),
    #拉取评论列表
    url(r'^get_comments/$',views.get_comments,name='get_comments'),
    #发布Joke
    url(r'^publish/$',views.publish,name='publish'),
    #取消发布
    url(r'^unpublish/$',views.unpublish,name='unpublish'),
    #获取详情
    url(r'^joke/$',views.get_joke,name='get_joke'),

)
