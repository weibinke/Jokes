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
import datetime
import logging
import json
import xlrd
from django.core.management.base import BaseCommand
from optparse import make_option

logger=logging.getLogger("jokes.datahelper")
DEFAULT_AUTHOR_FILE = "/root/joke_project/jokes/datahelper/authors.xlsx"
#import Author from excel file
class Command(BaseCommand):
    option_list = BaseCommand.option_list + (
    make_option('--filepath', '-l', dest='filepath', action='append',help='the file path of author xlsx'),)
    help = 'the file path of author xlsx'
    def handle(self, **options):
        print "batchAddAuthor start filepath = %s" % options.get('filepath')
        logger.info("batchAddAuthor start filepath = %s" % options.get('filepath'))
        authorFilePath=options.get('filepath')[0]
        data = xlrd.open_workbook(authorFilePath)
        table = data.sheets()[0]
        nrows = table.nrows
        ncols = table.ncols
        logger.info("batchAddAuthor rows=%d,cols=%d" % (nrows,ncols) )
        for rownum in range(1,nrows):
            row = table.row_values(rownum)
            if row:
                author = Author(username=row[0],nickname=row[1],password=row[2],avatar=row[3])
                author.save()
                logger.debug("batchAddAuthor add author:" + row[0])
        logger.info("batchAddAuthor completed")
