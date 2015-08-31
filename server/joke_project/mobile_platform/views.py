# -*- coding:UTF-8 -*-
from django.shortcuts import render
from django.http import HttpResponse

def index(request):
    return HttpResponse("This is home!")


def checkupdate(request):
    return HttpResponse("checkupdate")

def checkadconfig(request):
    return HttpResponse("checkadconfig")
