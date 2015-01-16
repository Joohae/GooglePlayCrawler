# GooglePlayCrawler
A crawler for Google Play. The code is written in Java, write for coding test of Renzu.

1. Add a HTML parser
2. Write content extracting code from google play
  : https://play.google.com/store/apps/collection/topselling_free?hl=en
3. Write dao and database access code with SQLite
4. Write a report class to show result.
5. Reverse engineering for the Action of 'Show More' button.
	-> send 'start' and 'number' filed of post data.

Todo:

6. Identify the flag for 'end of list'. 
   It seems the number items in topselling_* list peg to 600.
7. Reverse engineering for the Action of 'Next comment page'
	- the url may https://play.google.com/store/getreviews
	  and post data are: reviewType 0, pageNum 2, id : package id, reviewSortOrder: 2

   
How to run:

1. before execute the program, you have to make certification file from the site.
	- connect the site and see the list.
	- extract certification from web browser.
	  I have saved the file in resource folder and named "DER_Encoded_Google_Certificate.cer"
	- convert the certificate file to .jks with following command.
	keytool -importcert -file DER_Encoded_Google_Certificate.cer -keystore googleplay.jks -storepass 123456
	  Note: 'keytool' is a part of Java Development Kit.
