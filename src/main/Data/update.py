import pandas as pd

# read papers.csv and authors.csv
papers_df = pd.read_csv('papers.csv')
authors_df = pd.read_csv('authors.csv')

# filter rows that have not null fields
papers_df = papers_df[papers_df['abstract'].notna() & papers_df['title'].notna() & papers_df['year'].notna() & papers_df['full_text'].notna()]

# Checking that we have at least 200 samples with not null "abstract" field
if len(papers_df) < 200:
    raise ValueError("Not enough samples with with not null 'abstract' field")

# take 200 samples
sampled_papers_df = papers_df.sample(n=200)

# join based on source_id
merged_df = pd.merge(sampled_papers_df, authors_df, on='source_id', how='inner')

# Combine first_name and last_name to one field called "authors"
merged_df['authors'] = merged_df.apply(lambda row: row['first_name'] + ' ' + row['last_name'], axis=1)

# make it in the right format
merged_df = merged_df[['authors','year', 'title', 'abstract', 'full_text']]

# group by 'title' and join all the author's name in one row
grouped_df = merged_df.groupby('title').agg({
    'authors': ', '.join,
    'year': 'first',
    'abstract': 'first',
    'full_text': 'first'
}).reset_index()


# Export data to corpus.csv
grouped_df = grouped_df[['authors','year', 'title', 'abstract', 'full_text']]
grouped_df.to_csv('corpus.csv', index=False)

print("Data was exported to corpus.csv.")