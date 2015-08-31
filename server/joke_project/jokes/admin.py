from django.contrib import admin
from jokes.models import Joke
from jokes.models import Author
from jokes.models import Image
from jokes.models import DeviceInfo
from jokes.models import Like
from jokes.models import Comment
from jokes.models import ThumbUp
# Register your models here.

admin.site.register(Joke)
admin.site.register(Author)
admin.site.register(Image)
admin.site.register(DeviceInfo)
admin.site.register(Like)
admin.site.register(Comment)
admin.site.register(ThumbUp)
