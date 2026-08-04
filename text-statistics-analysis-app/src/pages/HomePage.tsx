import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

type Project = {
  id: number;
  name: string;
};

export async function getProjects(): Promise<Project[]> {
  const response = await fetch(`http://localhost:8080/getProjects`);
  const data: Project[] = await response.json();
  return data;
}

export default function HomePage() {
    const [projects, setProjects] = useState<Project[]>([]);

    function AddProjectInput() {
        const [name, setName] = useState<string>("");

        async function upload() {
            if(name == "") return;

            const response = await fetch(`http://localhost:8080/createProject?name=${encodeURIComponent(name)}`, {method: "POST"});
            if (response.ok) {
                getProjects().then(setProjects);
            } else {
                alert("Failed to add project.");
            }
        }

        return (
            <>
                <input type="text" placeholder="New project name" value={name || ""} 
                onChange={(e) => setName(e.target.value)} 
                onKeyDown={(e) => {
                    if(e.key === "Enter") {
                        upload();
                    }
                }}/>
                <button disabled={!name.trim()} onClick={upload}>Add Project</button>
            </>
        )
    }

    useEffect(() => {
        getProjects().then(setProjects);
    }, []);

    return (
    <>
        <h1>Main page</h1>
        <section>
            <h2>Projects</h2>
            <ProjectsList projects={projects}/>
            <AddProjectInput/>
        </section>
    </>
    );
}

function ProjectsList({ projects }: { projects: Project[] }) {
    return (
        projects.map((project, index) => ([
            <section>
                <Link key={index * 2} to={`/search/${project.id}`}>{project.name}</Link>
                <Link key={index * 2 + 1} to={`/projects/${project.id}`}>Edit</Link>
            </section>
        ]))
    );
}