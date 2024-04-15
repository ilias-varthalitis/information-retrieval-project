
INTRODUCTION:

The aim of this project is to design and implement an information retrieval system for scientific articles. Using the Lucene
library, an open-source toolkit for text search engines, the system will enable users to efficiently search through a collec-
tion of academic papers.

CORPUS:
The corpus is a subset of the papers dataset. Our selection encompasses the title, year, abstract and full text fields. Addi-
tionally, we use the unique id to associate each paper with its respective author, allowing us to include the authors’ names
into the corpus.

TEXT ANALYSIS ABD INDEX CREATION
For the text analysis we use the Standard Analyzer which provides us a lot of utilities such as the removal of uppercases
and stopwords.To store our index files, we will use the FSDirectory class, which enables efficient storage on disk. Addi-
tionally we will use an IndexWriter object which is responsible for adding documents to the index.For that also we will
need to use Lucene’s Document class.

SEARCH
Our system will use Lucene’s QueryParser class for keyword-based searches. It will also offer field searches where users
can search within specific fields such as the title, abstract, or full text of the documents. Additionally, it will maintain a
search history which can be useful for more applications such as autocompletion and support searching by author’s names
which is a separate field in the document index.

RESULT REPRESENTATION
Once we receive the ordered results from the search, we will display them using a graphical user interface (GUI) following
the given rules. Only 10 results are going to be displayed per page with highlighted keywords and the ability to reorder
them based on the year that the paper was published.
