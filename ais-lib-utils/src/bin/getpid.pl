#!/usr/bin/perl -w

use strict;

print getpid( $ARGV[0] );

sub getpid {
	my $procname  = shift;
	my $ps_out    = qx[ps ax | grep "$procname"];
	my @processes = split( /\n/, $ps_out );
	foreach my $process (@processes) {
		$process =~ s/^\s+//;

		#print "process: $process\n";
		my @parts = split( /\s+/, $process );
		my $pid = $parts[0];

		#print "pid: $pid\n";
		my $app = $parts[4];

		#print "app: $app\n";
		if ( $app =~ m/java/ ) {
			return $pid;
		}
	}
	return "";
}
