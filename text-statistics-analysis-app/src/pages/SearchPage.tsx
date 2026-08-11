import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import './App.css'

type QueryMark = {
  start: number;
  end: number;
};

type SearchResult = {
  id: number;
  snippet: string;
  queryMarks: QueryMark[];
};

type CountEntry = {
  label: string;
  count: number;
};

type GeneralSearchStatistics =  {
  numResults: number;
  numDocuments: number;
}

type SearchResults = {
  generalStatistics: GeneralSearchStatistics;
  snippets: SearchResult[];
  characterCounts: CountEntry[];
  wordCounts: CountEntry[];
  bigramCounts: CountEntry[];
  trigramCounts: CountEntry[];
}

export async function getSearchResults(query: string, projectId: string): Promise<SearchResults> {
  const response = await fetch(`http://localhost:8080/getResults?query=${encodeURIComponent(query)}&projectId=${encodeURIComponent(projectId)}`);
  const data: SearchResults = await response.json();
  return data;
}

function App() {
  const { projectId } = useParams();

  const [projectName, setProjectName] = useState<string>("");
  const [searchText, setSearchText] = useState<string>("");
  const [searchStatistics, setSearchStatistics] = useState<GeneralSearchStatistics>({
    numResults: 0,
    numDocuments: 0
  });
  const [results, setResults] = useState<SearchResult[]>([]);
  const [characterCounts, setCharacterCounts] = useState<CountEntry[]>([]);
  const [wordCounts, setWordCounts] = useState<CountEntry[]>([]);
  const [bigramCounts, setBigramCounts] = useState<CountEntry[]>([]);
  const [trigramCounts, setTrigramCounts] = useState<CountEntry[]>([]);

  useEffect(() => {
    async function fetchProjectName() {
      const response = await fetch(`http://localhost:8080/getProjectById?projectId=${encodeURIComponent(projectId!)}`);
      if (response.ok) {
        const data = await response.json();
        setProjectName(data.name);
      }
    }
    fetchProjectName();
  }, [projectId]);

  async function handleSearch() {
    const results = await getSearchResults(searchText, projectId!);
    setSearchStatistics(results.generalStatistics);
    setResults(results.snippets);
    setCharacterCounts(results.characterCounts);
    setWordCounts(results.wordCounts);
    setBigramCounts(results.bigramCounts);
    setTrigramCounts(results.trigramCounts);
  }

  return (
    <>
      <div className="page-header">
        <Link className="header-link" to="/">Projects</Link>
        <h1 className="page-title">{projectName}</h1>
        <Link className="header-link" to={`/projects/${projectId}`}>Documents</Link>
      </div>
      <section>
        <input
          value={searchText}
          onChange={(e) =>
            setSearchText(e.target.value)
          }
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              handleSearch();
            }
          }}
        />
        <button onClick={handleSearch}>Search</button>
      </section>
      <div className="content">
        <section>
          <h2>Character Counts</h2>
          <FrequencyList counts={characterCounts}/>
          <h2>Word Counts</h2>
          <FrequencyList counts={wordCounts}/>
          <h2>Bigram Counts</h2>
          <FrequencyList counts={bigramCounts}/>
          <h2>Trigram Counts</h2>
          <FrequencyList counts={trigramCounts}/>
        </section>
        <section className="results">
          <SearchStatistics num_results={searchStatistics.numResults} num_documents={searchStatistics.numDocuments} />
          <SearchResults results={results} />
        </section>
      </div>
    </>
  )
}

function SearchStatistics({ num_results, num_documents }: { num_results: number; num_documents: number; }) {
  return (
    <div className="search-results">
      <div className="search-result-card">{`${num_results} result(s) found`}</div>
      <div className="search-result-card">{`results in ${num_documents} document(s)`}</div>
    </div>
  );
}

function SearchResults({ results }: { results: SearchResult[]; }) {
  return (
    <div className="search-results">
      {results.map(result => (
        <SearchResultCard key={result.id} result={result}/>
      ))}
    </div>
  );
}

function SearchResultCard({ result }: { result: SearchResult; }) {
  const elements: React.ReactNode[] = [];
  let current = 0;
  result.queryMarks.forEach((match, index) => {
    // Text before the highlight
    if(current < match.start) {
      elements.push(
        <span key={`text-${index}`}>
          {result.snippet.substring(current, match.start)}
        </span>
      );
    }
    // Highlighted text
    elements.push(
      <mark key={`mark-${index}`}>
        {result.snippet.substring(match.start, match.end)}
      </mark>
    );
    current = match.end;
  });

  // Remaining text
  if (current < result.snippet.length) {
    elements.push(
      <span key="end">
        {result.snippet.substring(current)}
      </span>
    );
  }
  return <div className="search-result-card">{elements}</div>;
}

function FrequencyList({ counts }: { counts: CountEntry[]; }) {
  counts.sort((a, b) => b.count - a.count);
  const maxCount = Math.max(...counts.map((d) => d.count));
  return (
    <div className="frequency-list">
      {counts.map((item) => (
        <div key={item.label} className="row">
          <div className="label" title={item.label}>
            {item.label}
          </div>
          <div className="bar-container" title={item.label}>
            <div className="bar" style={{width: `${(item.count / maxCount) * 100}%`,}}/>
          </div>
          <div className="count">{item.count}</div>
        </div>
      ))}
    </div>
  );
}

export default App
