use strict;
use warnings;

#Useful Script for converting multiple files:
#    ls *.txt | xargs -n1 perl pros.pl

my $DEBUG = 0; # Set to one when debugging to get terminal output

my $filename = $ARGV[0];

open (my $fh, '<:raw', $filename)
    or die "Could not open file '$filename' $!";

my @sections = ("","","","","","","","","","","","","","","","");
my $sectionNumber = 0;
my $chemicalName = "";

while (my $row = <$fh>) {
    chomp $row;

    if ($row =~ /Product Name: (.+) Catalog/) {
        $chemicalName = $1;
    }

    if ($row =~ /Section \d+: \w+/) {
        $sectionNumber++;
    } else {
        $row =~ s/\s/ /g; # replace tabs, newlines, etc, with one space
        $row =~ s/p. \d+//g; # remove page numbers
        $row =~ s/"//g; # remove double quotes to avoid errors when creating the json file
        $sections[$sectionNumber] .= $row;
    }
}

if ($DEBUG) {
    print "Chemical Name: $chemicalName\n\n";
}

my $json = "{\"Name\" : \"$chemicalName\",";

my $sect = $sections[3];
$sect =~ /(Potential Acute Health Effects): (.+) (Potential Chronic Health Effects): (.+)/;
$json .= "\"Hazards Identification\": {\"$1\":\"$2\",\"$3\":\"$4\"},";

if ($DEBUG) {
    print "Section 3:\n--------\n\n";
    print "$1\n\n$2\n\n$3\n\n$4\n\n";
}

$sect = $sections[4];
$sect =~ /(Eye Contact): (.+) (Skin Contact): (.+) (Serious Skin Contact): (.+) (Inhalation): (.+) (Serious Inhalation): (.+) (Ingestion): (.+) (Serious Ingestion): (.+)/;

$json .= "\"First Aid Measures\": {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\",\"$9\":\"$10\",\"$11\":\"$12\",\"$13\":\"$14\"},";

if ($DEBUG) {
    print "Section 4:\n---------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n$11 : $12\n$13 : $14\n\n";
}

$sect = $sections[5];
$sect =~ /(Flammability of the Product): (.+) (Auto-Ignition Temperature): (.+) (Flash Points): (.+) (Flammable Limits): (.+) (Products of Combustion): (.+) (Fire Hazards in Presence of Various Substances): (.+) (Explosion Hazards in Presence of Various Substances): (.+) (Fire Fighting Media and Instructions): (.+) (Special Remarks on Fire Hazards): (.+) (Special Remarks on Explosion Hazards): (.+)/;

$json .= "\"Fire and Explosion Data\": {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\",\"$9\":\"$10\"},";

if ($DEBUG) {
    print "Section 5:\n----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n\n";
}

$sect = $sections[6];
$sect =~ /(Small Spill): (.+) (Large Spill): (.+)/;

$json .= "\"Accidental Release Measures\": {\"$1\":\"$2\",\"$3\":\"$4\"},";

if ($DEBUG) {
    print "Section 6:\n----------\n\n";
    print "$1 : $2\n$3 : $4\n\n";
}

$sect = $sections[7];
$sect =~ /(Precautions): (.+) (Storage): (.+)/;

$json .= "\"Handling and Storage\": {\"$1\":\"$2\",\"$3\":\"$4\"},";

if ($DEBUG) {
    print "Section 7:\n----------\n\n";
    print "$1 : $2\n$3 : $4\n\n";
}

$sect = $sections[8];
$sect =~ /(Engineering Controls): (.+) (Personal Protection): (.+) (Personal Protection in Case of a Large Spill): (.+) (Exposure Limits): (.+)/;

$json .= "\"Exposure Controls and Personal Protection\": {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\"},";

if ($DEBUG) {
    print "Section 8:\n----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n\n";
}


$sect = $sections[9];
$sect =~ /(Physical state and appearance): (.+) (Odor): (.+) (Taste): (.+) (Molecular Weight): (.+) (Color): (.+) (pH \(1% soln\/water\)): (.+) (Boiling Point): (.+) (Melting Point): (.+) (Critical Temperature): (.+) (Specific Gravity): (.+) (Vapor Pressure): (.+) (Vapor Density): (.+) (Volatility): (.+) (Odor Threshold): (.+) (Water\/Oil Dist\. Coeff\.): (.+) (Ionicity \(in Water\)): (.+) (Dispersion Properties): (.+) (Solubility): (.+)/;

$json .= "\"Physical and Chemical Properties\" : {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\",\"$9\":\"$10\",\"$11\":\"$12\",\"$13\":\"$14\",\"$15\":\"$16\",\"$17\":\"$18\",\"$19\":\"$20\",\"$21\":\"$22\",\"$23\":\"$24\",\"$25\":\"$26\",\"$27\":\"$28\",\"$29\":\"$30\",\"$31\":\"$32\"},";

if ($DEBUG) {
    print "Section 9:\n----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n$11 : $12\n$13 : $14\n$15 : $16\n$17 : $18\n$19 : $20\n$21 : $22\n$23 : $24\n$25 : $26\n$27 : $28\n$29 : $30\n$31 : $32\n\n";
}

$sect = $sections[10];
$sect =~ /(Stability): (.+) (Instability Temperature): (.+) (Conditions of Instability): (.+) (Corrosivity): (.+) (Special Remarks on Reactivity): (.+) (Special Remarks on Corrosivity): (.+) (Polymerization): (.+)/;

$json .= "\"Stability and Reactivity Data\" : {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\",\"$9\":\"$10\",\"$11\":\"$12\",\"$13\":\"$14\"},";

if ($DEBUG) {
    print "Section 10:\n-----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n$11 : $12\n$13 : $14\n\n";
}

$sect = $sections[11];
$sect =~ /(Routes of Entry): (.+) (Toxicity to Animals): (.+) (Chronic Effects on Humans): (.+) (Other Toxic Effects on Humans): (.+) (Special Remarks on Toxicity to Animals): (.+) (Special Remarks on Chronic Effects on Humans): (.+) (Special Remarks on other Toxic Effects on Humans): (.+)/;

$json .= "\"Toxicological Information\" : {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\"$8\",\"$9\":\"$10\",\"$11\":\"$12\",\"$13\":\"$14\"},";

if ($DEBUG) {
    print "Section 11:\n-----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n$11 : $12\n$13 : $14\n\n";
}


$sect = $sections[12];
$sect =~ /(Ecotoxicity): (.+) (BOD5 and COD): (.+) (Products of Biodegradation): (.+) (Toxicity of the Products of Biodegradation): (.+) (Special Remarks on the Products of Biodegradation): (.+)/;

$json .= "\"Ecological Information\" : {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\",\"$7\":\" $8\",\"$9\":\"$10\"},";

if ($DEBUG) {
    print "Section 12:\n-----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n$7 : $8\n$9 : $10\n\n";
}

$sect = $sections[13];
$sect =~ /(Waste Disposal): (.+)/;

$json .= "\"Disposal Considerations\" : {\"$1\":\"$2\"},";

if ($DEBUG) {
    print "Section 13:\n-----------\n\n";
    print "$1 : $2\n\n";
}

$sect = $sections[14];
$sect =~ /(DOT Classification): (.+) (Identification): (.+) (Special Provisions for Transport): (.+)/;
$json .= "\"Transport Information\" : {\"$1\":\"$2\",\"$3\":\"$4\",\"$5\":\"$6\"},";
if ($DEBUG) {
    print "Section 14:\n-----------\n\n";
    print "$1 : $2\n$3 : $4\n$5 : $6\n\n";
}

$sect = $sections[15];
$sect =~ /Health Hazard: (.+) Fire Hazard: (.+) Reactivity: (.+) Personal Protection: (.*) National/;
my $upper = uc($4);

$json .= "\"Health\" : $1, \"Flammability\" : $2, \"Instability\" : $3, \"Notice\" : \"$upper\"";

if ($DEBUG) {
    print "Section 15:\n-----------\n\n";
    print "Health $1, Fire $2, React $3, Personal Protection $4\n\n";
}

# END JSON
$json .= "}";

if (!$DEBUG) {
    $filename =~ /(.+)\.txt/;
    my $file = "$1.json";

    unless(open FILE, '>'.$file) {
        die "\nUnable to create $file\n";
    }

    # Write some text to the file.
    print FILE "$json\n";
    close FILE;
}
