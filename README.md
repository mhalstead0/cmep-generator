# cmep-generator
This thrown-together CMEP generator utility demonstrates how consistent (but randomized) CMEP files can be generated.

A sample input file is included in the data directory.

Usage: java com.matthalstead.cmepgenerator.Main -i <input> [-o <output>] -u <usageDate> [-z <timeZone>]
 -i,--input <arg>       Input file
 -o,--output <arg>      Output file (defaults to stdout if not specified)
 -u,--usageDate <arg>   Usage date ('yyyy-MM-dd')
 -z,--timeZone <arg>    Time zone (e.g. 'America/New_York',
                        'America/Chicago')

