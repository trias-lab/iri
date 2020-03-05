SGX 交易所的分析报告

\1.  

​     SGX的本质是提供加密服务，类似一个黑盒子，在 SGX内部可以进行加密计算、加密存储、加密传输，从而提供安全性的保障。

  这里使用SGX来做交易所的想法，最初的目的是将数据作为资产来保护，希望通过各种手段将用户的数据最终加密存储在SGX内部，交易的时候也在SGX内部进行计算，从而使交易数据对交易所的运营方也是保密的，用户的数据真正属于用户所有，保管方无法获得和分析用户的数据。这一点可以与现有的云平台进行对比。只要用户如果了解加密相关的基本知识，并知道保护自己的数据，那么久应该能够理解这个方案跟其他方案的区别。

 

\2.  

​     至于为什么要做交易所，是因为使用交易所的人比较多，借此希望能够得到更多的用户。这里的交易所就专门指加密货币的交易所。

  目前的加密货币交易所大多数是集中式的，如币安Binance、火币huobi、Coinbase等等，他们手里掌握着大量的用户数据和交易数据，在提供服务的同时收取一定的费用。

  另外有去中心化交易所的实现，实现起来可能更加复杂，性能会差一些。

 

\3.  

​     鉴于交易所的功能实现比较复杂，尤其是核心的交易撮合系统，我们实施起来最好是基于开源软件做修改来实现。我调研了一下开源的交易所。

1） bisp https://github.com/bisq-network/bisq 这个是使用 java 开发的，目前还是持续更新中，github 上的 star 数有 3k，应该是个比较靠谱的项目。

有个小问题： sgx 能否使用JAVA代码还须确定，之前有看到文章说，sgx 内部已经可以跑 jvm 了。如果不行的话，需要跨语言技术。

 

2）peatio https://github.com/peatio/peatio 这个是使用 ruby实现的，star 数有 3.3k，但是已经5年没有更新了，是李笑来投资的一个项目

 

3）rubykube 是基于上面的 peatio 项目的再开发版本，也是ruby 郁郁，有针对多个加密货币的交易所，有针对bch 的，https://github.com/rubykube/peatio-bitcoincash， 有针对莱特币的，https://github.com/rubykube/peatio-litecoin，代码也都是偶尔更新

 

4）viabtc https://github.com/viabtc/viabtc_exchange_server 是使用 c 开发的，大部分代码在3年前完成，2019年偶尔更新一次。star 有 2.2k

 

总结一下，peatio 是GitHub 上star 数目最多的，但是有点太老了，而且是ruby语言；bisp是目前还活跃的，但是使用的是 java 语言；viabtc 使用的是 C 语言，也有点老，主力开发人员就一个人，质量上不知道有没有保障。

 

\4. 

​     如果不使用现成的交易所，基于开源的撮合引擎然后再开发交易所，是能够减轻一些工作难度的。下面是几个开源的撮合引擎：

1）liquibook https://github.com/enewhuis/liquibook c++语言

2）exchange-core https://github.com/mzheravin/exchange-core java语言

3）cpptrader https://github.com/chronoxor/CppTrader c++语言

4）matching_engine https://github.com/fmstephe/matching_engine go 语言

5）LightMatchingEngine https://github.com/gavincyi/LightMatchingEngine python 语言

....

 

 

\5. 仔细看下来，其实交易所的业务逻辑是比较复杂的，需要投入一些人力和资源，我们可以第一步先用SGX打造一个对数据资产的“瑞士银行”，用于保存用户的重要数据或者隐私数据。

做法大致如下：

可以采用C/S架构，也可以采用B/S架构，先假设C/S架构。

client端：

​     使用户创建一个自己的秘钥，然后将关键数据进行加密，发送到server端保存，可以类     似于一个APP或者钱包；

server端：

​     将数据保存在SGX内部，SGX内部进行数据的保存（可以以文件的形式或者数据库的形 式，具体方式待定）。

server与client 之间可以使用https的加密通信。