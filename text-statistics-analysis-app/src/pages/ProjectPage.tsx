import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";

type Document = {
  id: number;
  name: string;
};

type ProjectDocument = {
    projectId: number;
    documentId: number;
}

export async function getDocuments(): Promise<Document[]> {
  const response = await fetch(`http://localhost:8080/getDocuments`);
  const data: Document[] = await response.json();
  return data;
}

export async function getProjectsDocuments(projectId: string): Promise<ProjectDocument[]> {
  const response = await fetch(`http://localhost:8080/getProjectDocuments?projectId=${encodeURIComponent(projectId)}`);
  const data: ProjectDocument[] = await response.json();
  return data;
}

export default function ProjectPage() {
    const { projectId } = useParams();

    const [projectName, setProjectName] = useState<string>("");
    const [documents, setDocuments] = useState<Document[]>([]);
    const [projectDocuments, setProjectDocuments] = useState<ProjectDocument[]>([]);

    useEffect(() => {
        async function fetchProjectName() {
            const response = await fetch(`http://localhost:8080/getProjectById?projectId=${encodeURIComponent(projectId!)}`);
            if (response.ok) {
                const data = await response.json();
                setProjectName(data.name);
            }
        }
        fetchProjectName();
        getDocuments().then(setDocuments);
        getProjectsDocuments(projectId!).then(setProjectDocuments);
    }, [projectId]);

    function handleToggle(documentId: number, checked: boolean) {
        if(checked) {
            setProjectDocuments(prev => [...prev, { projectId: Number(projectId), documentId }]);
        }else {
            setProjectDocuments(prev =>
                prev.filter(d => d.documentId !== documentId)
            );
        }
        // Update the backend
        if(checked) {
            fetch(`http://localhost:8080/addDocumentToProject?projectId=${encodeURIComponent(projectId!)}&documentId=${encodeURIComponent(documentId)}`, {
                method: "POST"
            });
        } else {
            fetch(`http://localhost:8080/removeDocumentFromProject?projectId=${encodeURIComponent(projectId!)}&documentId=${encodeURIComponent(documentId)}`, {
                method: "POST"
            });
        }
    }

    function UploadFileInput() {
        const [file, setFile] = useState<File | null>(null);

        async function uploadDocument() {
            if(!file) return;

            const formData = new FormData();
            formData.append("file", file);
            const response = await fetch("http://localhost:8080/uploadDocument", {
            method: "POST",
            body: formData,
            });

            if (response.ok) {
                setFile(null);
                getDocuments().then(setDocuments);
            } else {
                alert("Upload failed.");
            }
        }

        return (
            <>
            <button onClick={uploadDocument} disabled={!file}>Upload document file</button>
            <input
                type="file"
                accept=".txt"
                onChange={(e) => setFile(e.target.files?.[0] ?? null)}
            />
            </>
        );
    }

    return (
    <>
        <h1>{projectName}</h1>
        <Link to="/">Home</Link>
        <Link to={`/search/${projectId}`}>Search</Link>
        <section>
            <h2>Documents</h2>
            <DocumentsList documents={documents} projectDocuments={projectDocuments} onToggle={handleToggle} />
            <UploadFileInput/>
        </section>
    </>
    );
}

function DocumentsList({documents, projectDocuments, onToggle}: {documents: Document[]; projectDocuments: ProjectDocument[]; onToggle: (documentId: number, checked: boolean) => void}) {
    return (
        <>
            {documents.map(document => {
                const checked = projectDocuments.some(
                    d => d.documentId === document.id
                );

                return (
                    <section key={document.id}>
                        <label>
                            {document.name}
                            <input type="checkbox" checked={checked} onChange={(e) => onToggle(document.id, e.target.checked)}/>
                        </label>
                    </section>
                );
            })}
        </>
    );
}