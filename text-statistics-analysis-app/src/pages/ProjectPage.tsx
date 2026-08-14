import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";

type Document = {
  id: string;
  name: string;
};

type ProjectDocument = {
    projectId: string;
    documentId: string;
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

    function handleToggle(documentId: string, checked: boolean) {
        if(checked) {
            setProjectDocuments(prev => [...prev, { projectId: projectId!, documentId }]);
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
                method: "DELETE"
            });
        }
    }

    function deleteDocument(documentId: string) {
        fetch(`http://localhost:8080/deleteDocumentById?documentId=${encodeURIComponent(documentId)}`, {method: "DELETE"})
        .then(() => {
            getDocuments().then(setDocuments);
        });
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
        <div className="page">
            <section className="page-header">
                <h1 className="project-page-title">{projectName}</h1>
                <Link className="header-link" to={`/search/${projectId}`}>Analysis</Link>
                <Link className="header-link" to="/">Projects</Link>
            </section>
            <section>
                <h2>Documents</h2>
                <DocumentsList documents={documents} projectDocuments={projectDocuments} onToggle={handleToggle} deleteDocument={deleteDocument} />
                <UploadFileInput/>
            </section>
        </div>
    </>
    );
}

function DocumentsList({documents, projectDocuments, onToggle, deleteDocument}: {documents: Document[]; projectDocuments: ProjectDocument[]; onToggle: (documentId: string, checked: boolean) => void; deleteDocument: (documentId: string) => void}) {
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
                            <button onClick={() => {deleteDocument(document.id)}}>
                                Delete
                            </button>
                        </label>
                    </section>
                );
            })}
        </>
    );
}