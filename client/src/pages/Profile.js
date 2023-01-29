import styled from 'styled-components';
import PostBox from '../components/PostBox';
import UserInfo from '../components/UserInfo';
import userStore from '../store/userStore';
import { useParams } from 'react-router-dom';
import dummyData from '../db/dummyData.json';
import { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
const Profile = () => {
  const data = dummyData.posts;
  const params = useParams();
  const userId = params.userId;
  const userStoreId = userStore((state) => state.userId);
  const [codi, setCodi] = useState([]);
  // console.log(userId);
  useEffect(() => {
    window.scrollTo(0, 0);
    const fetchData = async () => {
      try {
        const response = await axios.get(`http://13.125.30.88/boards`);
        setCodi(response.data.data);
        console.log(response.data.data);
      } catch {
        window.alert('오류가 발생했습니다.');
      }
    };
    fetchData();
  }, []);
  const myCodi = useMemo(() => {
    return codi.filter((codi) => {
      return codi.member?.memberId === Number(userId);
    });
  }, [codi]);
  console.log(myCodi);
  return (
    <>
      <SWrapper>
        <div className="profil">
          {/* <Sidebar /> */}
          <div className="main-post">
            <UserInfo />
            <Sline></Sline>
            {userId == userStoreId ? (
              <Filter>
                <SCodi>
                  <SMyCodi>My Codi</SMyCodi>
                  <SLikeCodi>Like Codi</SLikeCodi>
                </SCodi>
              </Filter>
            ) : (
              ''
            )}
            <PostBox data={myCodi} />
          </div>
        </div>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
  .profil {
    width: 73%;
    display: flex;
    justify-content: flex-start;
  }
  .main-post {
    width: 70%;
  }
`;
const Filter = styled.div`
  display: flex;
  justify-content: center;
  height: 500px;
  button {
    margin: 50px;
    font-size: 20px;
    width: 140px;
    height: 40px;
    background-color: #ece9ca;
    display: flex;
    align-items: center;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 10px;
  }
`;

const SMyCodi = styled.button`
  margin: 30px;
  font-size: 30px;
  width: 150px;
  height: 50px;
  margin-right: 100px;
  margin-left: 100px;
  border-radius: 20px;
  justify-content: center;
  display: flex;
`;

const SLikeCodi = styled.button`
  margin: 30px;
  font-size: 30px;
  width: 150px;
  height: 50px;
  border-radius: 20px;
  justify-content: center;
  display: flex;
`;

const SCodi = styled.div`
  display: flex;
  justify-content: center;
`;

const Sline = styled.hr`
  display: flex;
  justify-content: center;
  width: 90%;
  margin-right: 20px;
`;

export default Profile;
