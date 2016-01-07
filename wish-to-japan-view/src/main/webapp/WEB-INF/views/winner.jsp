<%@ page import="org.jug.brainmaster.model.response.WinnerResponse" %>
<%@ page import="java.util.List" %>
<div class="win-wrapper">
  <a class='btn' href=''>Lihat Pemenang Periode 1</a>
  
  <h2 class="title">
    <div class='winner-qty' id='japan-winner-qty'></div>
    Pemenang Liburan Gratis ke Jepang
  </h2>
  <ol id='japan-winner-list'>
    <%
      List<WinnerResponse> japan = (List<WinnerResponse>) request.getAttribute("japan");
      if (japan != null) {
        for (WinnerResponse p : japan) {
          out.print("<li>" + p.getName() + " <span>" + p.getVoucherCode() + "</span></li>");
        }
      }
    %>
  </ol>

  <h2 class="title">Pemenang Hadiah Menarik Lainnya</h2>
  <img src='_asset/images/gambar-hadiah.jpg' alt='my big wish | blibli' style="display:block;"/>

  <h2>
    <div class='winner-qty' id='macbook-winner-qty'></div>
    pemenang apple macbook pro
  </h2>
  <ol id='macbook-winner-list'>
    <%
      List<WinnerResponse> macbook = (List<WinnerResponse>) request.getAttribute("macbook");
      if (macbook != null) {
        for (WinnerResponse p : macbook) {
          out.print("<li>" + p.getName() + " <span>" + p.getVoucherCode() + "</span></li>");
        }
      }
    %>
  </ol>

  <h2>
    <div class='winner-qty' id='fujifilm-winner-qty'></div>
    pemenang kamera fujifilm x-m1
  </h2>
  <ol id='fujifilm-winner-list'>
    <%
      List<WinnerResponse> fujifilm = (List<WinnerResponse>) request.getAttribute("fujifilm");
      if (fujifilm != null) {
        for (WinnerResponse p : fujifilm) {
          out.print("<li>" + p.getName() + " <span>" + p.getVoucherCode() + "</span></li>");
        }
      }
    %>
  </ol>

  <h2>
    <div class='winner-qty' id='lenovo-winner-qty'></div>
    pemenang lenovo phab plus
  </h2>
  <ol id='lenovo-winner-list'>
    <%
      List<WinnerResponse> phabplus = (List<WinnerResponse>) request.getAttribute("phabplus");
      if (phabplus != null) {
        for (WinnerResponse p : phabplus) {
          out.print("<li>" + p.getName() + " <span>" + p.getVoucherCode() + "</span></li>");
        }
      }
    %>
  </ol>

  <h2>
    <div class='winner-qty' id='meizu-winner-qty'></div>
    pemenang meizu m2
  </h2>
  <table width="100%" cellpadding="0" cellspacing="0" id='meizu-winner-table'>
    <tr>
      <%
        StringBuilder left = new StringBuilder().append(
          "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" id='meizu-left-column-table'>");
        StringBuilder right = new StringBuilder().append(
          "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" id='meizu-right-column-table'>");

        List<WinnerResponse> meizu = (List<WinnerResponse>) request.getAttribute("meizu");
        if (meizu != null) {
          boolean leftSide = true;
          for (WinnerResponse p : meizu) {
            if (leftSide) {
              left.append("<tr class='tr-blok'><td>" + p.getName() + "</td><td class='right'>" + p
                .getVoucherCode() + "</td></tr>");
              leftSide = false;
            } else {
              right.append("<tr class='tr-blok'><td>" + p.getName() + "</td><td class='right'>" + p
                .getVoucherCode() + "</td></tr>");
              leftSide = true;
            }
          }
        }
        left.append("</table>");
        right.append("</table>");
      %>
      <td class="td-blok">
        <%
          out.print(left.toString());
        %>
      </td>
      <td class="td-blok">
        <%
          out.print(right.toString());
        %>
      </td>
    </tr>
  </table>

</div>
