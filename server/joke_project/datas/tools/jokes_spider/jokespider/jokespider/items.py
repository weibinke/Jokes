# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class JokespiderItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    author = scrapy.Field()
    image = scrapy.Field()
    content = scrapy.Field()
    id = scrapy.Field()
    authorid = scrapy.Field()
    authorimg = scrapy.Field()
    votes = scrapy.Field()
    comments = scrapy.Field()
    tag = scrapy.Field()

    pass
