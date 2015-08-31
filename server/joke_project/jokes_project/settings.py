#coding=utf-8

"""
Django settings for jokes_project project.

For more information on this file, see
https://docs.djangoproject.com/en/1.6/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.6/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

JOKE_CONFIG_FILE_PATH = '/etc/jokes/joke.conf'

#导入模块
import logging
import django.utils.log
import logging.handlers
import ConfigParser
import sys,os

cf = ConfigParser.ConfigParser()
cf.read(JOKE_CONFIG_FILE_PATH)

STATIC_ROOT=BASE_DIR 
LOGGING = {
    'version': 1,
    'disable_existing_loggers': True,
    'formatters': {
       'standard': {
            'format': '%(asctime)s [%(threadName)s:%(thread)d] [%(name)s:%(lineno)d] [%(module)s:%(funcName)s] [%(levelname)s]- %(message)s'}  #日志格式 
    },
    'filters': {
    },
    'handlers': {
        'mail_admins': {
            'level': 'ERROR',
            'class': 'django.utils.log.AdminEmailHandler',
            'include_html': True,
        },
        'default': {
            'level':'DEBUG',
            'class':'logging.handlers.RotatingFileHandler',
            'filename': os.path.join(STATIC_ROOT+'/logs/','all.log'),     #日志输出文件
            'maxBytes': 1024*1024*5,                  #文件大小 
            'backupCount': 5,                         #备份份数
            'formatter':'standard',                   #使用哪种formatters日志格式
        },
        
        'error': {
            'level':'ERROR',
            'class':'logging.handlers.RotatingFileHandler',
            'filename': os.path.join(STATIC_ROOT+'/logs/','all.log'),
            'maxBytes':1024*1024*5,
            'backupCount': 5,
            'formatter':'standard',
        },
        'console':{
            'level': 'DEBUG',
            'class': 'logging.StreamHandler',
            'formatter': 'standard'
        },
        'request_handler': {
            'level':'DEBUG',
            'class':'logging.handlers.RotatingFileHandler',
            'filename': os.path.join(STATIC_ROOT+'/logs/','all.log'), 
            'maxBytes': 1024*1024*5, 
            'backupCount': 5,
            'formatter':'standard',
        },
        'scprits_handler': {
            'level':'DEBUG',
            'class':'logging.handlers.RotatingFileHandler',
            'filename':os.path.join(STATIC_ROOT+'/logs/','all.log'), 
            'maxBytes': 1024*1024*5, 
            'backupCount': 5,
            'formatter':'standard',
        }
    },
    'loggers': {
        'django': {
            'handlers': ['default','console'],
            'level': 'DEBUG',
            'propagate': False 
        },
        'django.request': {
            'handlers': ['request_handler'],
            'level': 'DEBUG',
            'propagate': False,
        },
        'scripts': { 
            'handlers': ['scprits_handler'],
            'level': 'INFO',
            'propagate': False
        },
        'jokes': {
            'handlers': ['default', 'error'],
            'level': 'DEBUG',
            'propagate': True
        },
    } 
}

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.6/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
# SECRET_KEY = '=&hux=7r_@m4k-q&h%46rmm=b5w54uf6c6p&46yyk-e_jryy6%'
SECRET_KEY = cf.get("secret_key","secret_key")

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = []


# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'jokes',
    'mobile_platform',
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'jokes_project.urls'

WSGI_APPLICATION = 'jokes_project.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.6/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': cf.get("mysql","db_name"),
        'USER': cf.get("mysql","user"),
        'PASSWORD': cf.get("mysql","password"),
        'HOST': cf.get("mysql","host"), 
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.6/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.6/howto/static-files/

STATIC_URL = '/static/'

TEMPLATE_DIRS = [os.path.join(BASE_DIR),'template']
