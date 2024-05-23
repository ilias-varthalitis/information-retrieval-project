**About**

This is a desktop app built with JavaFX, capable of searching for scientific papers. 
It supports both search using a keyword and field specific search by selecting from the drop-down menu. 
The user can also have access to their search history, delete it if they wish and 
make a previously searched query again. Results are displayed in batches of 10 where
the keyword is highlighted and can be sorted by year.

**How to run**
1. Download and extract the zip of the project to your PC.
2. Open the project using Intellij
3. Navigate to the FrontEnd package under src/main subfolder and run the main function in gui.java

**Data used**

[This](https://www.kaggle.com/datasets/rowhitswami/nips-papers-1987-2019-updated/data?select=papers.csv)
is the dataset that was used for the app. A python script helped us filter and extract
200 randomly selected papers into a single corpus.csv file, found under the Data package
in src/main subfolder. Therefore, we combine and keep only the columns we need from both
CSVs from Kaggle into one file. The corpus was subsequently indexed using the Lucene 
library and its APIs.