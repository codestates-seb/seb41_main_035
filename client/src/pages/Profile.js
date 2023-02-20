import styled from 'styled-components';
import PostBox from '../components/PostBox';
import UserInfo from '../components/UserInfo';
import userStore from '../store/userStore';
import { useParams } from 'react-router-dom';
import { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
import { BREAK_POINT_TABLET, token } from '../constants/index';

const Profile = () => {
  const params = useParams();
  const userId = params.userId;
  const userStoreId = userStore((state) => state.userId);
  const [codi, setCodi] = useState([]);
  const [followData, setFollowData] = useState([]);
  const API_URL = process.env.REACT_APP_API_URL;
  //추가부분
  const [codiType, setCodiType] = useState('my');

  localStorage.setItem('myId', JSON.stringify(userStoreId));
  console.log(userStoreId);

  useEffect(() => {
    window.scrollTo(0, 0);
    const fetchData = async () => {
      try {
        const response = await axios.get(API_URL + `boards`, {
          // 토큰 추가
          headers: { Authorization: token },
        });
        setCodi(response.data.data);
      } catch {
        window.alert('오류가 발생했습니다.');
      }
    };
    fetchData();
  }, []);

  const myCodi = useMemo(() => {
    console.log(userId);
    return codi.filter((codi) => {
      return codi.member?.memberId === Number(userId);
    });
  }, [codi, userId]);

  //추가부분
  const likeCodi = useMemo(() => {
    return codi.filter((codi) => {
      return codi.like === true;
    });
  }, [codi, userId]);
  console.log('likeCodi', likeCodi);

  return (
    <>
      <SWrapper>
        <div className="profil">
          <div className="main post">
            <UserInfo />
            <Sline></Sline>
            {userId == userStoreId ? (
              <Filter>
                <SCodi>
                  <SMyCodi onClick={() => setCodiType('my')}>My Codi</SMyCodi>
                  <SLikeCodi onClick={() => setCodiType('like')}>
                    Like Codi
                  </SLikeCodi>
                </SCodi>
              </Filter>
            ) : (
              ''
            )}
            {/* 타입이 my 이면 Mycodi, 아니면 LikeCodi */}
            <PostBox data={codiType === 'my' ? myCodi : likeCodi} />
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
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 100%;
        justify-content: center;
      }
    }
  }
  .main-post {
    width: 70%;
  }
`;
const Filter = styled.div`
  display: flex;
  justify-content: center;
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
  border: none;
  box-shadow: 1px 1px 6px gray;
  cursor: pointer;
  :hover {
    box-shadow: 2px 2px 3px gray;
  }
`;

const SLikeCodi = styled.button`
  margin: 30px;
  font-size: 30px;
  width: 150px;
  height: 50px;
  border-radius: 20px;
  justify-content: center;
  display: flex;
  border: none;
  box-shadow: 1px 1px 6px gray;
  cursor: pointer;
  :hover {
    box-shadow: 2px 2px 3px gray;
  }
`;
const SCodi = styled.div`
  display: flex;
  justify-content: center;
`;
const Sline = styled.hr`
  display: flex;
  justify-content: center;
  width: 95%;
  margin-right: 20px;
`;

export default Profile;
