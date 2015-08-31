# -*- coding: utf-8 -*-
import scrapy
from jokespider.items import JokespiderItem

class CodeshomeSpider(scrapy.Spider):
    name = "codeshome"
    allowed_domains = ["m.qiushibaike.com"]
    #每天拉取糗事百科前面5页的内容
    start_urls = (
        'http://m.qiushibaike.com/hot',
        'http://m.qiushibaike.com/hot/page/2',
        'http://m.qiushibaike.com/hot/page/3',
        'http://m.qiushibaike.com/hot/page/4',
        'http://m.qiushibaike.com/hot/page/5',
        'http://m.qiushibaike.com/hot/page/6',
        'http://m.qiushibaike.com/hot/page/7',
        'http://m.qiushibaike.com/hot/page/8',
    )

    def parse(self, response):
        items=[]
        y = response.xpath("//div[@class='article block untagged mb15']")

        for index,i in enumerate(y):
            item = JokespiderItem()
            author = i.xpath(".//div[@class='author']/a/text()")
            if len(author) > 1 :
#                print "author len %d,value is %s" % (len(author) , author)
#            item['author'] = i.xpath(".//div[@class='author']/a/text()")[1].extract()
                item['author'] = author[1].extract()
            if len(i.xpath(".//div[@class='thumb']/a/img/@src")) :
                item['image'] = i.xpath(".//div[@class='thumb']/a/img/@src")[0].extract()
            else :
                item['image'] = ""    
            item['id'] = i.xpath(".//@id").extract_first()
            item['authorid'] = i.xpath(".//div[@class='author']/a/@href").extract_first()
            item['authorimg'] = i.xpath(".//div[@class='author']/a/img/@src").extract_first()
            item['votes'] = i.xpath(".//div[@class='stats']/span[@class='stats-vote']/i/text()").extract_first()
            item['comments'] = i.xpath(".//div[@class='stats']/span[@class='stats-comments']/a/i/text()").extract_first()
            item['tag'] = i.xpath(".//div[@class='stats']/span[@class='stats-tag']/a/text()").extract()
            item['content'] = i.xpath(".//div[@class='content']/text()").extract_first()
#            print "content xxxx is :" + str(i.xpath("//div[@class='content']/text()").extract())
            items.append(item)

        print "parse end count is %d" % (len(items))
        return items
