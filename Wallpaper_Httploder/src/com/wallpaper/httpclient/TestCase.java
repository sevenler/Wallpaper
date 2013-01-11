package com.wallpaper.httpclient;

public class TestCase {

	public static void main(String[] args) {
		MultiThreadedHttpLoader loader = new MultiThreadedHttpLoader();
		try {
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
			loader.execute("http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1800_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=http://t1.baidu.com/it/u=521504115,204347455&fm=17");
			loader.execute("http://svn.apache.org/viewvc/httpcomponents/");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		loader.shutdown();
	}

}
