<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN">
<head profile="http://gmpg.org/xfn/11">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>/dev/null 2&gt;&amp;1 详解 | micmiu - 大大的技术 | 小小的生活</title>
	<link rel="stylesheet" type="text/css" media="all" href="http://www.micmiu.com/wp-content/themes/zbench/style.css" />
	<link rel='shortlink' href='http://www.micmiu.com/?p=814' />
</head>
<body class="single single-post postid-814 single-format-standard">

<div class="entry">
			<p>我们在shell 脚本命令中会经常看到类似这样内容：<span style="color: #ff0000;">/dev/null 2&gt;&amp;1</span>，这条命令的意思是将标准输出和错误输出全部重定向到 /dev/null 空设置中,也就是将产生的所有信息丢弃。</p>
<p><span style="color: #0000ff;">[一]、命令的解释</span></p>
<p><span style="color: #ff0000;"> &gt;/dev/null 2&gt;&amp;1</span> 可以拆分开易于理解：</p>
<ol>
<li><span style="line-height: 22px;"><span style="color: #ff0000;">&gt;</span> ：代表重定向到哪里，例如：echo “micmiu.com” &gt; /home/michaeltest.txt<br />
</span></li>
<li><span style="line-height: 22px;"><span style="color: #ff0000;">/dev/null</span> ：代表空设备文件<br />
</span></li>
<li><span style="line-height: 22px;"><span style="color: #ff0000;">2&gt;</span> ：表示stderr标准错误<br />
</span></li>
<li><span style="line-height: 22px;"><span style="color: #ff0000;">&amp;</span> ：表示等同于的意思，2&gt;&amp;1，表示2的输出重定向等同于1<br />
</span></li>
<li><span style="line-height: 22px;"><span style="color: #ff0000;">1</span> ：表示stdout标准输出，系统默认值是1，所以”&gt;/dev/null”等同于 “1&gt;/dev/null”<br />
</span></li>
</ol>
<p>故：”&gt;/dev/null 2&gt;&amp;1″ 也可以写成 “1&gt;/dev/null  2&gt;&amp;1″</p>
<p><span style="color: #0000ff;">[二]、命令的执行过程</span></p>
<p><span style="color: #ff0000;">1&gt;/dev/null</span> ：首先表示标准输出重定向到空设备文件，也就是不输出任何信息到终端，说白了就是不显示任何信息。<br />
<span style="color: #ff0000;">2&gt;&amp;1</span> ：接着，标准错误输出重定向到标准输出，因为之前标准输出已经重定向到了空设备文件，所以标准错误输出也重定向到空设备文件。</p>
<p>本文介绍到此结束@<a href="http://www.micmiu.com/" >Michael Sun</a>.</p>
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br /><br />
<br />
<br />
<br />
<br />
asdasd
<br />
<br />
<br />
<br />
</div>

</body>
</html>