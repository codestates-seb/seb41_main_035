import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';
import ImageInput from '../components/ImageInput';
import PlusButton from '../components/Plusbutton';
import { useState } from 'react';
import axios from 'axios';
import { token, BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';

const PostUpload = () => {
  const [contentList, setContentList] = useState([
    {
      productImage: [],
      brand: '',
      productName: '',
      size: '',
      price: '',
      link: '',
      rental: false,
      rentalPrice: 0,
      category: '아우터',
    },
    {
      productImage: [],
      brand: '',
      productName: '',
      size: '',
      price: '',
      link: '',
      rental: false,
      rentalPrice: 0,
      category: '아우터',
    },
  ]); // PostUploadBar에 전달 , defaultContent기본값 1개뜨게끔
  const [inputContent, setInputContent] = useState(); // textarea 입력값저장
  const [imgFile, setImgFile] = useState([]); // 업로드한 이미지 배열저장
  console.log('contentList', contentList);

  const onChangeItem = (index, key, value) => {
    setContentList((preContentList) => {
      const newContentList = preContentList;

      if (key === 'productImage') {
        newContentList[index][key].push(...value); // 여기서의 value는 배열
      } else {
        newContentList[index][key] = value;
      }
      console.log('newContentList', newContentList);
      return newContentList;
    });
  };
  const onPost = () => {
    let formData = new FormData();
    formData.append('userImage', imgFile[0]); //메인 사진
    formData.append('content', JSON.stringify(inputContent)); //게시글
    for (let i = 0; i < contentList.length; i++) {
      formData.append(
        'products[' + i + '].productImage',
        contentList[i].productImage[0]
      );
      console.log(contentList[i].productImage);
      formData.append('products[' + i + '].brand', contentList[i].brand);
      formData.append(
        'products[' + i + '].productName',
        contentList[i].productName
      );
      formData.append('products[' + i + '].size', contentList[i].size);
      formData.append('products[' + i + '].price', contentList[i].price);
      formData.append('products[' + i + '].link', contentList[i].link);
      formData.append('products[' + i + '].rental', contentList[i].rental);
      formData.append(
        'products[' + i + '].rentalPrice',
        contentList[i].rentalPrice
      );
      formData.append('products[' + i + '].category', contentList[i].category);
    }
    if (!inputContent) {
      alert('게시글을 입력해주세요.');
      return;
    } else {
      axios
        .post('http://13.125.30.88/boards', formData, {
          headers: {
            Authorization: token,
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((res) => {
          if (res) {
            location.href = '/';
          }
          console.log(res.data);
        })
        .catch((err) => {
          return err;
        });
    }
    for (let [key, value] of formData.entries()) {
      console.log(key + ':' + value);
    }
  };

  //plusButton클릭시 호출
  const addContent = () => {
    setContentList((prev) => [
      ...prev,
      {
        productImage: [],
        brand: '',
        productName: '',
        size: '',
        price: '',
        link: '',
        rental: false,
        rentalPrice: 0,
        category: '아우터',
      },
    ]);
  };

  return (
    <Section>
      <Scontainer>
        <SHeader>
          <div className="image_upload">
            <button type="submit" onClick={onPost}>
              완료
            </button>
          </div>
        </SHeader>

        <SMid>
          {/* 이미지파일첨부 */}
          <ImageInput imgFile={imgFile} setImgFile={setImgFile} />
          <div className="input_box">
            <textarea
              placeholder="게시글을 작성하세요."
              value={inputContent}
              onChange={(e) => setInputContent(e.target.value)}
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
  display: flex;
  justify-content: center;
  margin-left: 200px;
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
  height: 375px;
  border-radius: 6px;
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
    height: 310px;
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
export default PostUpload;
