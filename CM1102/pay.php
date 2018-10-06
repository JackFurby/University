<?php
session_start();
if (!isset($_SESSION['user_id'])) {
	$_SESSION['user_id'] = uniqid();
}
?>

<noscript>You need javascript enabled</noscript>

<!DOCTYPE html>
<meta charset="utf-8" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<html>
	<head id="header">
		<link rel="stylesheet" type="text/css" href="css/pay.css">
		<meta charset="utf-8">
		<title>Retro Tech - Pay</title>
		<!--javascript start-->
		<script type="text/javascript">

		//empty cart of successfull payment
		function emptyCart() {
			if (window.XMLHttpRequest) {
		    xhttp = new XMLHttpRequest();
		  } else {
		    // code for IE6, IE5
		    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    //document.getElementById("txtHint").innerHTML = this.responseText;
		    }
			};
			xhttp.open("GET", "emptyCart.php", true);
			xhttp.send();
		}

		function showHelpMsg(helpMsg) { //show help message on focus
			document.getElementById(helpMsg).style.display = "block";
		}

		function hideHelpMsg(helpMsg) {
			document.getElementById(helpMsg).style.display = "none";
		}

		function validatePayment(payForm) {

			var cardNumber = payForm.cardNumber.value;
			var cardNumberError = true;
			var exDateMonth = payForm.expirationDateMonth.value;
			var exDateMonthError = true;
			var exDateYear = payForm.expirationDateYear.value;
			var exDateYearError = true;
			var holderName = payForm.cardholderName.value;
			var holderNameError = true;
			var cardCode = payForm.securityCode.value;
			var cardCodeError = true;
			var dateYear = new Date().getFullYear().toString().substr(2,2);
			var exDateAll = true;
			var dateMonth = new Date().getMonth() + 1;


			//if statements for RegExp of form items

			if (payForm.cardTypeVisa.checked) {
				if (new RegExp("^4[0-9]{12}(?:[0-9]{3})?$").test(cardNumber)) { //RegExp for visa cards
					cardNumberError = false;
					document.getElementById("cardNumberError").style.display = "none";
				} else {
					cardNumberError = true;
					document.getElementById("cardNumberError").style.display = "inline-block";
				}
			} else if (payForm.cardTypeAmex.checked) {
				if (new RegExp("^3[47][0-9]{13}$").test(cardNumber)) { //RegExp for American Express cards
					cardNumberError = false;
					document.getElementById("cardNumberError").style.display = "none";
				} else {
					cardNumberError = true;
					document.getElementById("cardNumberError").style.display = "inline-block";
				}
			} else if (payForm.cardTypeMasterCard.checked) {
				if (new RegExp("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$").test(cardNumber)) { //RegExp for Master Card
					cardNumberError = false;
					document.getElementById("cardNumberError").style.display = "none";
				} else {
					cardNumberError = true;
					document.getElementById("cardNumberError").style.display = "inline-block";
				}
			}else {
					cardNumberError = true;
					document.getElementById("cardNumberError").style.display = "inline-block";
			}

			if (new RegExp("^(1[0-2]|0[1-9])$").test(exDateMonth)) {
				exDateMonthError = false;
			} else {
				exDateMonthError = true;
			}

			if (new RegExp("^[0-9]{2}$").test(exDateYear)) {
				exDateYearError = false;
			} else {
				exDateYearError = true;
			}

			if (new RegExp("^([a-zA-Z]|\\s|[’-]|[À-ſ])+$").test(holderName)) {
				holderNameError = false;
				document.getElementById("nameError").style.display = "none";
			} else {
				document.getElementById("nameError").style.display = "inline-block";
				holderNameError = true;
			}
			/*deedpoll. (n.d.). Printable characters on our deed poll documents. [online] Available at: http://www.deedpoll.org.uk/Forms/PrintableCharacters.pdf [Accessed 7 Mar. 2017].*/

			if (payForm.cardTypeAmex.checked) {
				if (new RegExp("^[0-9]{4}$").test(cardCode)) { //if card == American Express 4 digit number is required
					cardCodeError = false;
					document.getElementById("securityCodeError").style.display = "none";
				} else {
					cardCodeError = true;
					document.getElementById("securityCodeError").style.display = "inline-block";
				}
			}else {
					if (new RegExp("^[0-9]{3}$").test(cardCode)) { //else 3 digit number is required
						cardCodeError = false;
						document.getElementById("securityCodeError").style.display = "none";
					} else {
						cardCodeError = true;
						document.getElementById("securityCodeError").style.display = "inline-block";
					}
				}

			/*if statement to check date value*/
			if (parseInt(exDateYear) > parseInt(dateYear)) {
				exDateAll = false; //if expiration year is higher than current year
			} else if (parseInt(exDateYear) == parseInt(dateYear) && parseInt(exDateMonth) >= parseInt(dateMonth)){
				exDateAll = false; //if expiration year is == current year and expiration month is the same or higher
			}else {
				exDateAll = true;
			}
			if (exDateMonthError || exDateYearError || exDateAll) {
				document.getElementById("exDateError").style.display = "inline-block";
			} else {
				document.getElementById("exDateError").style.display = "none";
			}

			if (cardNumberError || exDateMonthError || exDateYearError || holderNameError || cardCodeError || exDateAll) {
				return false;
			} else {
				alert("Details accepted");
				emptyCart();
				return true;
			}
		}
		</script>
		<!--javascript end-->
	</head>
	<body>
			<!--Site nav bar start-->
      <nav>
        <ul id="taskBar">
          <li id="menuBtn"><a href="home.php" >Retro Tech</a></li>
          <li id="cart"><a href="cart.php">Cart</a></li>
        </ul>
      </nav>
			<!--Site nav bar end-->
			<!--Site background start-->
      <div id="bField"></div>
			<!--Site background end-->
			<!--Site title window start-->
			<div id='title' onclick="window.location='home.php';">
				<div class="winControl">
					<ul>
	          <li id="XDead"><a>X</a></li>
	        </ul>
				</div>
				<h1>Retro Tech</h1>
			</div>
			<!--Site title window end-->
			<!--Site cart window start-->
			<div id='cartSideBtn' onclick="window.location='cart.php';">
				<h1>Cart</h1>
			</div>
			<!--Site cart window end-->
			<!--Site menu window start-->
			<div id='siteWindow'>
				<div class="winControl">
					<ul>
	          <li id="XDead"><a>X</a></li>
						<li class='windowTitle' style='float:left'><h3>Categories</h3></li>
	        </ul>
				</div>
				<div id="categoriesNav">
					<ul id="productCatNav">
						<?php
						//loggin details for mysql
						$servername = "csmysql.cs.cf.ac.uk";
						$username = "c1619450";
						$password = "nibnugbat6";
						$dbname = "c1619450";
						//connect to mysql
						$con = mysqli_connect($servername, $username, $password, $dbname);
						//error checking for connecting to mysql
						if (!$con) {
							die("Failed to connect: " . mysqli_connect_error());
						}
						// command to get categorys from DB
						$commandCat = "SELECT DISTINCT category FROM cm1102_products";
						// Execute the query for categorys
						$resultCat = mysqli_query($con, $commandCat);
						while($rowCat = mysqli_fetch_assoc($resultCat)) {
							echo"<li><a href='home.php#".$rowCat['category']."'>".$rowCat['category']."</a></li>";
						}
						?>
					</ul>
				</div>
			</div>
			<!--Site menu window end-->
			<!--payment section start-->
			<div id='siteWindow'>
				<div class='winControl'>
					<ul>
						<li id='XDead'><a>X</a></li>
						<li class='windowTitle' style='float:left'><h3>Pay Here</h3></li>
					</ul>
				</div>
				<!--payment form start-->
				<?php
				if ((isset($_SESSION[$_cart_items])) && (!empty($_SESSION[$_cart_items])))  {

					$cart_total = 0.00;

					$array_counted = array_count_values($_SESSION[$_cart_items]);

					foreach ($array_counted as $item_in_cart => $quantity) {

						//loggin details for mysql
						$servername = "csmysql.cs.cf.ac.uk";
						$username = "c1619450";
						$password = "nibnugbat6";
						$dbname = "c1619450";
						//connect to mysql
						$con = mysqli_connect($servername, $username, $password, $dbname);
						//error checking for connecting to mysql
						if (!$con) {
							die("Failed to connect: " . mysqli_connect_error());
						}
						// command to get categorys from DB
						$commandCat = "SELECT price FROM cm1102_products WHERE ID = $item_in_cart";
						// Execute the query for categorys
						$result = mysqli_query($con, $commandCat);
						while($row = mysqli_fetch_assoc($result)) {

							$item_total = (float)$row["price"] * (int)$quantity;
							$cart_total += $item_total;
							$cart_total_format = "£ ".number_format($cart_total, 2);

						}
					}
					echo"
					<div id='cartTotal'>
						<p>Cart total: $cart_total_format</p>
					</div>

					<form name=\"shopPaymentForm\" onsubmit=\"return validatePayment(this)\" method=\"post\">
						<h3>Card type</h3>
						<div class=\"paymentForm\" onfocus=\"return showHelpMsg(this)\">
							<input type=\"radio\" name=\"cardType\" value=\"Visa\" id=\"cardTypeVisa\"><label for=\"cardTypeVisa\">Visa</label><br>
							<input type=\"radio\" name=\"cardType\" value=\"American Express\" id=\"cardTypeAmex\"><label for=\"cardTypeAmex\">American Express</label><br>
							<input type=\"radio\" name=\"cardType\" value=\"MasterCard\" id=\"cardTypeMasterCard\"><label for=\"cardTypeMasterCard\">Master Card</label><br>
							<span class=\"paymentHelpMsg\">Select the type of card you will be paying with. This should be displayed on your card.</span>
						</div>
						<h3>Card Number</h3>
						<div class=\"paymentForm\" id=\"cardNumberArea\">
							<input class='textIn' name=\"cardNumberIn\" placeholder=\"Card Number\" id=\"cardNumber\" maxlength=\"16\" onfocus=showHelpMsg(\"payHelpMsgCardNumber\") onfocusout=hideHelpMsg(\"payHelpMsgCardNumber\")>
							<p id=\"cardNumberError\">You have to enter a 16 digit number.</p>
							<span class=\"paymentHelpMsg\" id=\"payHelpMsgCardNumber\">Enter your card number here. Only enter numbers without spaces e.g. 4234567891234567.</span>
						</div>
						<h3>Expiration Date</h3>
						<div class=\"paymentForm\">
							<input class='textIn' name=\"expirationDateMonth\" placeholder=\"MM\" id=\"expirationDateMonth\" maxlength=\"2\" onfocus=showHelpMsg(\"payHelpMsgExDate\") onfocusout=hideHelpMsg(\"payHelpMsgExDate\")>
							<input class='textIn' name=\"expirationDateYear\" placeholder=\"YY\" id=\"expirationDateYear\" maxlength=\"2\" onfocus=showHelpMsg(\"payHelpMsgExDate\") onfocusout=hideHelpMsg(\"payHelpMsgExDate\")>
							<p id=\"exDateError\">Incorrect expiration date. Please ensure you enter it in the format MM YY.</p>
							<span class=\"paymentHelpMsg\" id=\"payHelpMsgExDate\">Enter the expiry date on your card. The date should be on the front of your card. Enter in the format MM YYYY e.g. 01 2018</span>
						</div>
						<h3>Name on card</h3>
						<div class=\"paymentForm\">
							<input class='textIn' name=\"cardholderName\" cols=\"25\" placeholder=\"Name on Card\" id=\"cardholderName\" onfocus=showHelpMsg(\"payHelpMsgName\") onfocusout=hideHelpMsg(\"payHelpMsgName\")>
							<p id=\"nameError\">Enter the name on the front of your card</p>
							<span class=\"paymentHelpMsg\" id=\"payHelpMsgName\">Enter the cardholders name on your card. This will be your name most of the time</span>
						</div>
						<h3>Security Code</h3>
						<div class=\"paymentForm\">
							<input class='textIn' name=\"securityCode\" placeholder=\"123\" id=\"securityCode\" maxlength=\"4\" onfocus=showHelpMsg(\"payHelpMsgSecurityCode\") onfocusout=hideHelpMsg(\"payHelpMsgSecurityCode\")>
							<p id=\"securityCodeError\">Enter the 3 or 4 digit security code on the back of your card.</p>
							<span class=\"paymentHelpMsg\" id=\"payHelpMsgSecurityCode\">Enter the security code on your card. This will be 3 or 4 digit long and on the back of your card or front for American Express.</span>
						</div>
						<h3>Submit</h3>
						<input type=\"submit\" name=\"pay\" value=\"Submit\">
						<input type=\"reset\" name=\"reset\" value=\"Reset\">
					</form>";
				} else {
					echo"
					<div id='cartTotal'>
						<p>Nothing in cart</p>
					</div>";
				}
						?>
				<!--payment form end-->
			 </div>
			 <!--payment section end-->
	</body>
</html>
