import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';
import Avatar from '../components/Avatar';
import ImageInput from '../components/ImageInput';
import PlusButton from '../components/Plusbutton';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { token, BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import { useParams, useNavigate } from 'react-router-dom';
const EditPost = () => {
  const url = 'http://13.125.30.88';
  const boardId = JSON.parse(localStorage.getItem('boardId'));
  const [editData, setEditData] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(url + `/boards/` + [boardId]);
        setEditData(response.data);
        console.log(response.data);
      } catch (err) {
        return err;
      }
    };
    fetchData();
  }, []);
  return (
    <Section>
      <Scontainer>
        <SHeader>
          <div className="image_upload">
            <button type="submit">완료</button>
          </div>
        </SHeader>

        <SMid>
          {/* 이미지파일첨부 */}
          <Avatar image={editData.userImage} />
          <div className="input_box">
            <textarea placeholder="게시글을 작성하세요."></textarea>
          </div>
        </SMid>
        {editData.products.map((index) => {
          return <PostUploadBar key={index} index={index} />;
        })}

        {/* {editData &&
          editData.map((content, index) => {
            return <PostUploadBar key={index} index={index} />;
          })} */}
      </Scontainer>
    </Section>
  );
};

const Section = styled.div`
  display: flex;
  justify-content: center;
  margin-left: 165px;
  min-width: 500px;

  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    margin-left: 0px;
  }
`;
const Scontainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const SHeader = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-end;
  border-bottom: 1px solid #b3b3b3;
  margin: 10px;

  .image_upload {
    /* width: 44vw; */
    /* @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      width: 540px;
    } */
  }
  button {
    width: 50px;
    height: 30px;
    margin: 15px;
    background-color: #d9d4a6;
    color: #ffffff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    /* box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px; */
  }
`;

const SMid = styled.form`
  display: flex;
  width: 100%; //* 추가
  margin: 10px 0px;
  background-color: #eee6ca;
  /* @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    width: 540px;
  } */
  .input_box {
    width: 80%; //* 추가
    margin: 10px;
    display: flex;
    /* justify-content: end; */
    align-content: center;
    flex-wrap: wrap;
  }
  textarea {
    font-size: 14px;
    box-sizing: border-box;
    height: 33vh;
    width: 100%; //* 추가
    /* width: 28vw; */
    border: none;
    border-radius: 5px;
    background-color: #ffffff;
    resize: none; /* 사용자가 텍스트사이즈 임의 변경 불가 */

    /* 1200px보다 작은화면에서는 아래와 같이 보이게 */
    /* @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      width: 350px;
    } */
  }
`;
export default EditPost;
