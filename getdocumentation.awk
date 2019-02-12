BEGIN {
  isapidocs = 0
  customsection = 0
} {

  if (isapidocs == 1 && $0 ~ /\/\//) {
    print $0
  } else { 
   isapidocs = 0 
  } 

  if ($0 ~ /\/\/ Method/) {
    isapidocs = 1
    print ""
    print ""
    print $0
  }
} END {

}
