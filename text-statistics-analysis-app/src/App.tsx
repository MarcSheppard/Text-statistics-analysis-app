import { useState } from "react";
import './App.css'

type SearchResult = {
  id: number;
  snippet: string;
};

type CountEntry = {
  label: string;
  count: number;
};

type Counts = {
  maxCount: number;
  countEntries: CountEntry[];
};

const MOCK_DATA: SearchResult[] = [
  {
    id: 1,
    snippet: "React is a JavaScript library for building user interfaces."
  },
  {
    id: 2,
    snippet: "Vite provides a fast development experience."
  },
  {
    id: 3,
    snippet: "TypeScript adds static typing to JavaScript."
  },
  {
    id: 4,
    snippet: "Search functionality can be mocked during development."
  },
  {
    id: 5,
    snippet: "Search functionality can be mocked during development."
  },
  {
    id: 6,
    snippet: "Search functionality can be mocked during development."
  },
  {
    id: 7,
    snippet: "Search functionality can be mocked during development."
  },
  {
    id: 8,
    snippet: "Search functionality can be mocked during development."
  }
];

export async function search(query: string): Promise<SearchResult[]> {
  // temporary implementation

  return MOCK_DATA.filter(item =>
    item.snippet.toLowerCase().includes(query.toLowerCase())
  );
}

function App() {
  const [searchText, setSearchText] = useState<string>("");
  const [results, setResults] = useState<SearchResult[]>([]);
  const [characterCounts, setCharacterCounts] = useState<Counts>({maxCount: 0, countEntries: []});
  const [wordCounts, setWordCounts] = useState<Counts>({maxCount: 0, countEntries: []});
  const [chunkCounts, setChunkCounts] = useState<Counts>({maxCount: 0, countEntries: []});

  async function handleSearch() {
    const found = await search(searchText);
    setResults(found);
    setCharacterCounts(getCharacterFrequencies(found[0].snippet));
    setWordCounts(getCharacterFrequencies(found[0].snippet))
    setChunkCounts(getCharacterFrequencies(found[0].snippet))
  }

  function handleFileChange(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    const selectedFile = event.target.files?.[0];

    if (selectedFile) {
      // Placeholder for future upload
      console.log("Selected file:", selectedFile.name);
    }
  }

  handleSearch();

  return (
    <>
      <h1>Project Name</h1>
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
        <input type="file" onChange={handleFileChange}/>
      </section>
      <div className="content">
        <section>
          <h2>Character Counts</h2>
          <FrequencyList counts={characterCounts}/>
          <h2>Word Counts</h2>
          <FrequencyList counts={wordCounts}/>
          <h2>Chunk Counts</h2>
          <FrequencyList counts={chunkCounts}/>
        </section>
        <section className="results">
          <SearchResults results={results} />
        </section>
      </div>
    </>
  )
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
  return (
    <div className="search-result-card">
      {result.snippet}
    </div>
  );
}

function FrequencyList({ counts }: { counts: Counts; }) {
  const maxCount = Math.max(...counts.countEntries.map((d) => d.count));

  return (
    <div className="frequency-list">
      {counts.countEntries.map((item) => (
        <div key={item.label} className="row">
          <div className="label">{item.label}</div>

          <div className="bar-container">
            <div
              className="bar"
              style={{
                width: `${(item.count / maxCount) * 100}%`,
              }}
            />
          </div>

          <div className="count">{item.count}</div>
        </div>
      ))}
    </div>
  );
}

function getCharacterFrequencies(text: string) {
  const counts: Counts = {
    maxCount: 28 + 20 + 14 + 11 + 9,
    countEntries: []
  };

  let countEntry: CountEntry = {
    label: "e",
    count: 28
  };
  counts.countEntries.push(countEntry);

  let countEntryB: CountEntry = {
    label: "a",
    count: 20
  };
  counts.countEntries.push(countEntryB);

  let countEntryC: CountEntry = {
    label: "t",
    count: 14
  };
  counts.countEntries.push(countEntryC);

  let countEntryD: CountEntry = {
    label: "l",
    count: 9
  };
  counts.countEntries.push(countEntryD);

  return counts;
}

export default App
