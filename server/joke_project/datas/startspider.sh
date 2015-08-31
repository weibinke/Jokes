#!/bin/bash
echo `pwd`
base_dir=$(cd "$(dirname "$0")"; pwd)
today=`date +%Y-%m-%d`
spiderpath="$base_dir/tools/jokes_spider/jokespider/"
result_dir="$base_dir/jokes/qiushibaike/results/$today"
jokes_result_file_path="$result_dir/jokes.json"
django_project_path="$base_dir/.."
author_xlsx_file_path="$base_dir/authors/authors.xlsx"
echo "base_dir is :$base_dir"
echo "spiderpath is :$spiderpath"
echo "jokes_result_file_path is :$jokes_result_file_path"
echo "django_project_path is :$django_project_path"
echo "author_xlsx_file_path is :$author_xlsx_file_path"

#执行strapy任务去爬取糗事百科
echo "==================开始爬取Jokes数据======================"
cd $spiderpath
rm -f $jokes_result_file_path
mkdir -p $result_dir
scrapy crawl codeshome -o $jokes_result_file_path -t json
echo "==================Jokes数据爬取完成======================"

echo "==================开始导入Author数据======================"
python $django_project_path/manage.py importauthor --filepath=$author_xlsx_file_path
echo "=================Author数据导入完成======================="

echo "==================开始导入Joke数据========================"
python $django_project_path/manage.py importjokes --filepath=$jokes_result_file_path
echo "==================Joke数据导入完成========================"


