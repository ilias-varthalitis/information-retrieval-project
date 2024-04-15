**Introduction**

The aim of this project is to design and implement an information retrieval system for scientific articles. Using the Lucene
library, an open-source toolkit for text search engines, the system will enable users to efficiently search through a collection of academic papers.

**Corpus**

The corpus is a subset of the papers dataset. Our selection encompasses the title, year, abstract and full text fields. Additionally, we use the unique id to associate each paper with its respective author, allowing us to include the authors’ names
into the corpus.

**Text analysis and index creation**

For the text analysis we use the Standard Analyzer which provides us a lot of utilities such as the removal of uppercases
and stopwords.To store our index files, we will use the FSDirectory class, which enables efficient storage on disk. Additionally we will use an IndexWriter object which is responsible for adding documents to the index.For that also we will
need to use Lucene’s Document class.

**Search**

Our system will use Lucene’s QueryParser class for keyword-based searches. It will also offer field searches where users
can search within specific fields such as the title, abstract, or full text of the documents. Additionally, it will maintain a
search history which can be useful for more applications such as autocompletion and support searching by author’s names
which is a separate field in the document index.

**Result representation**

Once we receive the ordered results from the search, we will display them using a graphical user interface (GUI) following
a given set of rules. Our system will show 10 results per page with highlighted keywords in each result and the ability to
reorder them based on the year that the paper was published.