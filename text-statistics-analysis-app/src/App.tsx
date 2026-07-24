import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import ProjectPage from "./pages/ProjectPage";
import SearchPage from "./pages/SearchPage";

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/projects/:projectId" element={<ProjectPage />} />
            <Route path="/search/:projectId" element={<SearchPage />} />
        </Routes>
    );
}