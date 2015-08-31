from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    url(r'^$', 'jokes_project.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^jokes/', include('jokes.urls')),
    url(r'^platform/', include('mobile_platform.urls')),
    url(r'^admin/', include(admin.site.urls)),
)
