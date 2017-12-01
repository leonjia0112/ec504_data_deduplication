TITLE
**************
Data Deduplication
**************

DESCRIPTION
**************
This application is for data duplication of ASCII file. Since the amount of data is generated in a
very high rate daily in the world, but not all the data that was generated is unique compare to the
existing data. To store those similar data, data deduplication is one efficient storage solution 
for this situation. Data deduplication storage seperate each file into pieces, for those duplicated
piece, only one piece is storage. Each file that has the content of this piece will have an pointer
to the piece, so it can retrieve the piece later. This approach saves the space of storage because 
it instead of storage same data over and over again, it only storage on copy of those data, 
original file can be assembled back by combine all the data pieces to rebuild the original data.
This is the idea of data deduplication.
**************

MEMBER
**************
Pei Jia (leojia@bu.edu)
Sijia Zhang ()
Powei Zhang ()
Zengyan Liu ()
Qifeng Xu ()
**************

FEATURES
**************
Here are all the features in this application 

Add Feature
	This feature is for user to add the file that needs to be deduplicated to the locker. User
	can either add one single file to the locker or they can add all the file in a directory
	to the locker. After the file is added to the locker. The file will be stored in the files
	of each locker.

Delete Feature
	This feature allows user to remove/delete file from the existing locker. This will not affect
	the other files, single only the chunk piece that is redundant will be removed from the locker
	if the chunk piece is still pointed by other files, it will remain in the locker. Whiling using
	delete feature, user can either delete one single file at a time or all the file undre the same
	directory name at a time.

Retrieve Feature
