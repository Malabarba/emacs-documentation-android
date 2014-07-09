Emacs Documentation
===========================

This is the project page for the *Emacs Documentation* android app.

For an actual description of the app (and for downloads), see the
project website
[here](http://bruce-connor.github.io/emacs-online-documentation/).

### Code Highlights ###

Code available here depends on
[HugeSQLiteCursor](https://github.com/Bruce-Connor/hugesqlitecursor/)
(an android lib developed by me), on my 
[utility classes](https://github.com/Bruce-Connor/util-android)
git submodule, and on
[actionbarsherlock](http://actionbarsherlock.com/).

Though the app does have a (narrow) target audience, this project
is mostly a test of concept and android-programming knowledge.  
It features the following:

1. A **very** fast reimplementation of SQLiteCursor
   ([developed by me](https://github.com/Bruce-Connor/hugesqlitecursor/)),
   with milliseconds responses even for queries on the order of 10000
   results.
2. A modern search interface with realtime results filtering from a
   huge sqlite database (thanks to the point above).
1. Modern tabbed navigation interface (compatible with old devices all
   the way to API 9, thanks to
   [actionbarsherlock](http://actionbarsherlock.com/)), which also features:
   2. Creation and deletion of tabs at runtime.
   3. "Pinned" tabs with stay in place.
   2. Context sensitive actionbar which displays buttons relevant to the current tab.
   3. Temporary tabs preseved through screen rotation.
3. Sending and receiving intents between external apps.
4. Intelligent context-sensitive Back Button navigation.
3. An intelligent preferences manager class which derives most options
   through usage. Thus, only a couple of preferences are left for the
   Settings menu.
4. Change color theme at runtime, as per user preference.
5. And some pretty standard stuff:
   1. Cursors and ListViews.
   2. SQLite database managing.
   3. Webview.
   4. Various Adapters.
   5. etc.
