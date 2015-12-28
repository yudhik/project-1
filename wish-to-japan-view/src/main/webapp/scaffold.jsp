<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html prefix="og: http://ogp.me/ns#">
<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
<meta content="utf-8" http-equiv="encoding">

<head>
  <title>Promo My Big Wish Live | Blibli.com</title>
  <meta charset="UTF-8">
  <meta name="description"
        content="Dapatkan voucher langsung 100 ribu untuk pembelian produk pilihan di blibli.com dengan cicilan 0% dan gratis pengiriman dan menangkan hadiah impianmu">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://www.blibli.com/wcsstore/Indraprastha/images/gdn/images/favicon.ico"
        rel="shortcut icon">
  <link rel="stylesheet" href="_asset/style.css">
  <link rel="stylesheet" href="_asset/css/main.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
  <script src="_asset/js/sweetalert.min.js"></script>
  <script src="_asset/js/reconnect.js"></script>
  <script src="_asset/js/main.js"></script>

  <!-- set Google tag manager section-->
  <!-- Google tag manager-->
  <noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PHC5L2" height="0" width="0"
            style="display:none;visibility:hidden"></iframe>
  </noscript>
  <script>(function (w, d, s, l, i) {
    w[l] = w[l] || [];
    w[l].push({'gtm.start': new Date().getTime(), event: 'gtm.js'});
    var f = d.getElementsByTagName(s)[0], j = d.createElement(
      s), dl = l != 'dataLayer' ? '&l=' + l : '';
    j.async = true;
    j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
    f.parentNode.insertBefore(j, f);
  })(window, document, 'script', 'dataLayer', 'GTM-PHC5L2');</script>
  <!-- End google tag manager-->

  <script>
    var _gaq = _gaq || [];
    var pluginUrl = 'https://www.google-analytics.com/plugins/ga/inpage_linkid.js';
    _gaq.push(['_require', 'inpage_linkid', pluginUrl]);
    _gaq.push(['_setAccount', 'UA-21718848-1']);
    _gaq.push(['_setDomainName', 'blibli.com']);
    _gaq.push(['_trackPageview']);
    (function () {
      var ga = document.createElement('script');
      ga.type = 'text/javascript';
      ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'https://www') + '.google-analytics.com/ga.js';
      var s = document.getElementsByTagName('script')[0];
      s.parentNode.insertBefore(ga, s);
    })();
  </script>

  <script type="text/javascript">
    var _bwaq = _bwaq || [];
    _bwaq.push(['_setAccount', 'blibli']);
    _bwaq.push(['_trackPageView', 'promo', '-1002']);

    (function () {
      var bwa = document.createElement('script');
      bwa.type = 'text/javascript';
      bwa.async = true;
      bwa.src = 'https://www.blibli.com/wcsstore/bwa.js'

      var s = document.getElementsByTagName('script')[0];
      s.parentNode.insertBefore(bwa, s);
    })();
  </script>

  <style type='text/css'>
  </style>

  <!--[if lt IE 9]>
  <script src="https://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>
<body>
<header id="branding" class="wide top-bar">
  <div class="row">
    <div class="half push-left branding">
      <a href="https://www.blibli.com/" class="logo-atas" title="go to blibli.com"><img
        src="https://www.blibli.com/resources/images/logo-blibli.png"
        alt="logo blibli | header"></a>
    </div>
  </div>
</header>

<div class="row">
  <div class='header'>
    <img src='_asset/images/header.png' alt='my big wish | blibli' style="display:block;"/>
  </div>

  <div class="slot-wrapper container">

    <div class="bg-awan">

      <div class="transparant"></div>

      <div id='pre-raffle-countdown-holder' class="hitungmundur" style="display: block;">
        <p>
          Bersiaplah! Pengundian pemenang akan dimulai dalam
        </p>
        <p><span id='pre-raffle-countdown' class='countdown'></span></p>
      </div>
      <script>
        $(document).ready(function () {
          var pageOpenTime = <%= (1451448000000L - new Date().getTime()) / 1000 %>;

          setInterval(function () {
            setTimer(pageOpenTime, $("#pre-raffle-countdown"))
          }, 1000);
          function setTimer(seconds, selector) {
            console.log(seconds);
            var finalDisplayTime = "";
            var day = Math.floor(seconds / (3600 * 24));
            var hour = Math.floor((seconds / 3600) % 24);
            var min = Math.floor((seconds / 60) % 60);
            var sec = Math.floor(seconds % 60);

            finalDisplayTime += showOrHideTimerPart(day, "hari");
            finalDisplayTime += showOrHideTimerPart(hour, "jam");
            finalDisplayTime += showOrHideTimerPart(min, "menit");
            finalDisplayTime += showOrHideTimerPart(sec, "detik");

            if (finalDisplayTime == "") {
              finalDisplayTime = "&nbsp;"
            }
            selector.html(finalDisplayTime);
            pageOpenTime -= 1;
          }
          function showOrHideTimerPart(timeDisp, str) {
            if(timeDisp > 0) return " " + timeDisp + " " + str;
          }
        });
      </script>

    </div>
  </div>
</div>

<footer class="wide">
  <div class="row">
    <a id="backhome" href="https://www.blibli.com/" class="btn push-left gotohome"
       title="go to blibli.com | footer"><img src="_asset/images/backhome.png" alt="home | footer"></a>
    <a id="backtop" class="btn push-right gototop" title="go to top | footer"><img
      src="_asset/images/backtop.png" alt="top | footer"></a>
    <div class="push-center text-center">
      <p>Copyright &copy; 2011-2015 Blibli.com toko online dengan sensasi belanja online store ala
        mall, All Rights Reserved</p>
    </div>
  </div>
</footer>
<script type="text/javascript">
  var viewHost = "<%=request.getAttribute("viewHost")%>";
  var useWss = "<%=request.getAttribute("useWss")%>";
</script>
<script>

</script>
<script src="_asset/js/jquery.easing.1.3.js" type="text/javascript" charset="utf-8"></script>
<script src="_asset/js/jquery.jSlots.min.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
