import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';
import ImageInput from '../components/ImageInput';
import PlusButton from '../components/Plusbutton';
import { useState } from 'react';

const PostUpload = () => {
  const defaultContent = {
    images: [],
    brandName: '',
    itemName: '',
    itemSize: '',
    itemPrice: '',
    itemSite: '',
    rentalCheck: false,
    rentalPrice: '',
  };

  const [contentList, setContentList] = useState([
    defaultContent,
    defaultContent,
  ]); // PostUploadBar에 전달할 데이터 , defaultContent기본값 2개 뜨게끔
  const [inputContent, setInputContent] = useState(); // textarea 입력값저장
  const [imgFile, setImgFile] = useState([]); // 업로드한 이미지 배열저장

  const onChangeItem = (index, key, value) => {
    setContentList((preContentList) => {
      const newContentList = preContentList;
      newContentList[index][key] = value;
      return newContentList;
    });
  };

  //plusButton클릭시 호출
  const addContent = () => {
    setContentList((prev) => [...prev, defaultContent]);
  };
  return (
    <Section>
      <Scontainer>
        <SHeader>
          <div className="image_upload">
            <button type="submit">완료</button>
          </div>
        </SHeader>

        <SMid>
          <ImageInput />
          <div className="input_box">
            <textarea
              placeholder="게시글을 작성하세요."
              // value={content}
              // onChange={(e) => setContent(e.target.value)}
            ></textarea>
          </div>
        </SMid>
        {contentList.map((content, index) => {
          return (
            <PostUploadBar
              key={index}
              index={index}
              onChangeItem={onChangeItem}
            />
          );
        })}
        <PlusButton onClick={addContent} />
      </Scontainer>
    </Section>
  );
};

const Section = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;
const Scontainer = styled.div`
  width: 70%;
  /* border: 1px solid green; */
`;

const SHeader = styled.div`
  display: flex;
  justify-content: center;
  /* border: 1px solid pink; */
  text-align: end;

  .image_upload {
    /* margin-left: 20px; */
    width: 43.5vw;
    margin-bottom: 10px;
    border-bottom: 1px solid #b3b3b3;
  }
  button {
    width: 3vw;
    height: 3vh;
    margin: 15px;
    background-color: #d9d4a6;
    color: #ffffff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
`;

const SMid = styled.div`
  display: flex;
  justify-content: center;
  margin: 10px 0px;

  /* border: 1px solid pink; */
  /* width: 30vw; */
  .input_box {
    /* width: 34vw; */
    margin-bottom: 10px;
    display: flex;
    justify-content: end;

    /* background-color: #b3b3b3; */
  }
  textarea {
    font-size: 14px;
    box-sizing: border-box;
    min-height: 25vh;
    width: 28vw;
    border: none;
    border-radius: 5px;
    background-color: #ffffff;
    resize: none; /* 사용자가 텍스트사이즈 임의 변경 불가 */
  }
`;
export default PostUpload;
