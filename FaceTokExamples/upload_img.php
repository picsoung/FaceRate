<?php
	print_r($_POST);
	$imgData = $_POST['imgData'];
	
	$target_path = "./images/";
	$type = 'png';

	$target_path = $target_path.time(); 
	
	$img = imagecreatefromstring(base64_decode($imgData)); 
	if($img != false) 
	{ 
	   imagejpeg($img, $target_path.'_img.jpg'); 
	} 
?>