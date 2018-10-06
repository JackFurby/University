<?php
session_start();
if (!isset($_SESSION['user_id'])) {
	$_SESSION['user_id'] = uniqid();
}
?>

<noscript>You need javascript enabled</noscript>
<!--Javascipt start-->
<script>

//adds item to cart and gives user feedback
function addToCart(item, msg) {

	if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
  } else {
    // code for IE6, IE5
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
			msg.style.display = 'inline-block';
			setTimeout(function(){
			msg.style.display = "none";
			},3000);
    }
	};
	console.log(item);
	xhttp.open("GET", "addToCart.php?value=" + item, true);
	xhttp.send();
}
</script>
<!--Javascipt end-->

<!DOCTYPE html>
<meta charset="utf-8" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<html>
	<head id="header">
		<link rel="stylesheet" type="text/css" href="css/product.css">
		<meta charset="utf-8">
		<title>Retro Tech - Product</title>
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
			//get product to display
			$ProID = $_GET['value'];

			//build command (better for sql injection)
			$command = $con->prepare("SELECT ID, name, description, price, image FROM cm1102_products WHERE ID = ?");
			$command->bind_param('s', $ProID);
			$command->execute();

			// Execute the query for products
			$result = $command->get_result();
	    //displays products
			while ($row = $result->fetch_assoc()) {
				echo"
				<!--Product section start-->
				<div id='siteWindow'>
					<div class='winControl'>
						<ul>
							<li id='XDead'><a>X</a></li>
							<li class='windowTitle' style='float:left'><h3>" . $row["name"]. "</h3></li>
						</ul>
					</div>
					<!--Product content start-->
					<div id='productCont'>
						<div id='itemImg'>
							<img src='".$row['image']."' alt='" . $row["name"]. "' style='width:400px;'>
						</div>
						<div id='itemInfoTop'>
							<div id='itemInfo'>
								<h4 id='itemName'>" . $row["name"]. "</h4>
								<p id='itemPrice'>£" . $row["price"]. "</p>
								<p id='itemID'>Product code: " . $row["ID"]. "</p>
							</div>
							<div id='itemAddCart'>
								<a value=" . $row["ID"]. "' id='productAddToCart' onclick='addToCart(" . $row["ID"]. ", this.nextSibling.nextElementSibling)'>Add to cart</a>
								<span class='cartMsg'>Product added to cart</span>
							</div>
						</div>
						<p id='itemDes'>". $row["description"]."</p>
					</div>
				</div>
        <!--Procuct content end-->
			 	<!--Product section end-->";
				}
				// Close the connection
				mysqli_close($connect);
				?>
	</body>
</html>
