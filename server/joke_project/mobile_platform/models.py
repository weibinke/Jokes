# -*- coding: UTF-8 -*-
from django.db import models
from datetime import datetime
import calendar

def timems2datetime(ms):
    return datetime.fromtimestamp(ms/1000.0)

def datetime2utctime(datetime):
    return long(calendar.timegm(datetime.utctimetuple())*1000.0 + datetime.microsecond /1000.0)

class VersionConfig(models.Model):
	PLATFORM_ANDROID = 0
	PLATFORM_IOS = 1

	#平台类型定义
	PLATFORM_TYPES=(
		(PLATFORM_ANDROID,'Android'),
		(PLATFORM_IOS,'iOS'),
	)

	appname = models.CharField(max_length= 100)
	package_name = models.CharField(max_length= 100)
	version_name = models.CharField(max_length= 100)
	platform = models.IntegerField(default=PLATFORM_ANDROID,choices=PLATFORM_TYPES)
	desc = models.CharField(max_length= 10240)
	version_code = models.IntegerField(default=0)
	pub_date = models.DateTimeField('create time',default=datetime.now())
    
	def __unicode__(self):
		return "%s %s V%s" % (VersionConfig.PLATFORM_TYPES[self.platform][1],self.appname,self.version_name); 
	pass
    

