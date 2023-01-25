// import { useState } from 'react';
import styled from 'styled-components';
import PropTypes from 'prop-types';
import React, { useState } from 'react';
//postuploadbar.js 에서 받아온 index,imgFile,onUploadImages 함수
const ItemImageInput = ({
  index,
  imgFile,
  onUploadImages,
  saveImagePreviewLinks,
}) => {
  // const [iamgeFile, setIamgeFile] = useState([]); // 이미지 배열
  const [fileImage, setFileImage] = useState([]);
  const onChangeImg = (e) => {
    const imageLists = e.target.files[0]; // 파일객체들
    let imageUrlLists = [...imgFile, imageLists];
    let imagePreLists = [];
    // for (let i = 0; i < imageLists.length; i++) {
    //   const currentImageUrl = URL.createObjectURL(imageLists[i]); // 이미지 미리보기 링크
    //   imageUrlLists.push(currentImageUrl);
    // }
    let reader = new FileReader();
    if (imageLists) {
      reader.readAsDataURL(imageLists);
    }
    reader.onloadend = () => {
      const resultImage = reader.result;
      imagePreLists.push(resultImage);
      setFileImage(imagePreLists);
      console.log(resultImage);
    };
    //1개만 업로드 가능
    if (imageUrlLists.length > 1) {
      imageUrlLists = imageUrlLists.slice(0, 1);
    }
    onUploadImages(imageUrlLists);
    saveImagePreviewLinks(imageUrlLists);
  };

  return (
    <SWrapper>
      <div className="image-upload ">
        <label htmlFor={`input-imgfile${index}`}>
          <div className="btn-upload">제품업로드</div>
        </label>
        <input
          type="file"
          id={`input-imgfile${index}`}
          onChange={onChangeImg} // 파일이 추가되면 이벤트가 일어난다.
          multiple // 파일 여러개 선택 가능
          accept="image/*" //모든 이미지 파일형식
        />
      </div>

      <SImagefiles>
        <div className="image-add">
          {/* input에 파일을 넣어줄때마다 state로 값을 배열로 저장해서, 배열의 길이만큼 이미지를 생성 */}
          {fileImage?.map((img, idx) => (
            <div className="image-info" key={idx}>
              <img src={img} alt="img" />
            </div>
          ))}
        </div>
      </SImagefiles>
    </SWrapper>
  );
};

ItemImageInput.propTypes = {
  index: PropTypes.number,
  imgFile: PropTypes.array,
  onUploadImages: PropTypes.func,
  saveImagePreviewLinks: PropTypes.func,
};

const SWrapper = styled.div`
  background-color: #eee6ca;

  .image-upload > input {
    display: none;
  }
  //파일 업로드버튼
  .btn-upload {
    width: 85px;
    height: 30px;
    /* padding: 7px 14px; */
    background-color: #d9d4a6;
    border-radius: 4px;
    color: white;
    font-size: 13px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-top: 15px;
  }
`;
const SImagefiles = styled.div`
  background-color: #eee6ca;
  .image-info {
    background-color: #eee6ca;
  }
  .image-add {
    display: flex;
    background-color: #eee6ca;
  }
  img {
    width: 85px;
    height: 88px;
    margin-left: 12px;
  }
`;
export default ItemImageInput;
