# -*- coding: utf-8 -*-
from django.conf.urls import patterns,url

from mobile_platform import views

urlpatterns = patterns('',
    url(r'^$',views.index,name='index'),
    url(r'^checkupdate/$',views.checkupdate,name='checkupdate'),
    url(r'^checkadconfig/$',views.checkadconfig,name='checkadconfig'),
)
