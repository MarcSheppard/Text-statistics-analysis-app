import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";

type Document = {
  id: number;
  name: string;
};

export async function getDocuments(): Promise<Document[]> {
  const response = await fetch(`http://localhost:8080/getDocuments`);
  const data: Document[] = await response.json();
  return data;
}

export async function getProjectsDocuments(projectId: string): Promise<Document[]> {
  const response = await fetch(`http://localhost:8080/getProjectDocuments?projectId=${encodeURIComponent(projectId)}`);
  const data: Document[] = await response.json();
  return data;
}

export default function ProjectPage() {
    const { projectId } = useParams();

    const [documents, setDocuments] = useState<Document[]>([
        { id: 1, name: "Document 1" },
        { id: 2, name: "Document 2" },
        { id: 3, name: "Document 3" },
    ]);
    const [projectDocuments, setProjectDocuments] = useState<Document[]>([]);

    useEffect(() => {
        getDocuments().then(setDocuments);
        getProjectsDocuments(projectId!).then(setProjectDocuments);
    }, [projectId]);

    function handleToggle(document: Document, checked: boolean) {
        if(checked) {
            setProjectDocuments(prev => [...prev, document]);
        }else {
            setProjectDocuments(prev =>
                prev.filter(d => d.id !== document.id)
            );
        }
    }

    return (
    <>
        <h1>Test: {projectId}</h1>
        <Link to="/">Home</Link>
        <Link to={`/search/${projectId}`}>Search</Link>
        <section>
            <h2>Documents</h2>
            <DocumentsList documents={documents} projectDocuments={projectDocuments} onToggle={handleToggle} />
            <UploadChangesInput/>
            <UploadFileInput/>
        </section>
    </>
    );
}

function DocumentsList({documents, projectDocuments, onToggle}: {documents: Document[]; projectDocuments: Document[]; onToggle: (document: Document, checked: boolean) => void}) {
    return (
        <>
            {documents.map(document => {
                const checked = projectDocuments.some(
                    d => d.id === document.id
                );

                return (
                    <section key={document.id}>
                        <label>
                            {document.name}
                            <input type="checkbox" checked={checked} onChange={(e) => onToggle(document, e.target.checked)}/>
                        </label>
                    </section>
                );
            })}
        </>
    );
}

export function UploadChangesInput() {
    async function upload() {
        const formData = new FormData();
        const response = await fetch("http://localhost:8080/uploadProjectDocuments", {
        method: "POST",
        body: formData,
        });

        if (response.ok) {
            alert("Upload successful!");
        } else {
            alert("Upload failed.");
        }
    }

    return (
        <button onClick={upload}>Upload changes</button>
    );
}

export function UploadFileInput() {
    const [file, setFile] = useState<File | null>(null);

    async function upload() {
        if(!file) return;

        const formData = new FormData();
        formData.append("file", file);
        const response = await fetch("http://localhost:8080/uploadDocument", {
        method: "POST",
        body: formData,
        });

        if (response.ok) {
            alert("Upload successful!");
            setFile(null);
        } else {
            alert("Upload failed.");
        }
    }

    return (
        <>
        <button onClick={upload} disabled={!file}>Upload document file</button>
        <input
            type="file"
            accept=".txt"
            onChange={(e) => setFile(e.target.files?.[0] ?? null)}
        />
        </>
    );
}