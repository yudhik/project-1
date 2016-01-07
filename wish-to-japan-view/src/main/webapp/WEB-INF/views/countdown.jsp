<%@ page session="false"%>
<div class="bg-awan">
  <div class="transparant"></div>

  <div id='pre-raffle-countdown-holder' class="hitungmundur" style="display: block;">
    <p>
      Bersiaplah! Pengundian pemenang akan dimulai dalam
    </p>
    <p><span id='pre-raffle-countdown' class='countdown'></span></p>
  </div>
</div>
<script>
  $(document).ready(function () {
    var pageOpenTime = ${countdownTime};

    setInterval(function () {
      if(pageOpenTime == 0) {
        location.reload();
      }
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
      if (timeDisp > 0) {
        return " " + timeDisp + " " + str;
      } else {
        return ""
      }
    }
  });
</script>
