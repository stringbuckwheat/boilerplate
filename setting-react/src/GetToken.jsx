import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";

const GetToken = () => {
    let {accessToken} = useParams();
    const navigate = useNavigate();

    console.log("accessToken", accessToken);

    const setUser = async () => {
        await localStorage.setItem("accessToken", accessToken);
        navigate("/main", {replace: true});
    }

    useEffect(() => {
        setUser();
    }, [])

    return (
        <></>
    )
}

export default GetToken;