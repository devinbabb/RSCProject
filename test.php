<?php
	$time = date('U');
	$time += 25200;
	$nextevent = 1279540800;
	$remaining = $nextevent - $time;
$left = secondsToTime($remaining);
echo("$left[h]:$left[m]:$left[s]");
echo("\n");
echo(date('r', $time));

	function secondsToTime($seconds)
	{
	// extract hours
		$hours = floor($seconds / (60 * 60));
	// extract minutes
		$divisor_for_minutes = $seconds % (60 * 60);
		$minutes = floor($divisor_for_minutes / 60);
	// extract the remaining seconds
		$divisor_for_seconds = $divisor_for_minutes % 60;
		$seconds = ceil($divisor_for_seconds);
	// return the final array
		$obj = array(
		"h" => (int) $hours,
		"m" => (int) $minutes,
		"s" => (int) $seconds,
		);
		return $obj;
	}

?>
