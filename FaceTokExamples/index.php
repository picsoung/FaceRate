<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<title>FaceTok</title>
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset-min.css">	
	<link href="style.css" rel="stylesheet" type="text/css">
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
	<script src="http://staging.tokbox.com/v0.91/js/TB.min.js" ></script>
	<script src="facetok.js"></script>
	
</head>

<body>
	<div id="main-container">
		<div id="stream-container" class="container">
			<div id="publisher-container">
				<div id="publisher"></div>				
			</div>
			<div class="button-container">
				<button id="identifyButton" style="display: none">Identify Person</button>
			</div>
		</div>
		<div id="log-container" class="container">
			<h2>Activity Log</h2>	
			<ul id="logs">	
			</ul>
		</div>
		<div id="image-container" class="container"></div>
	</div>

</body>
</html>
