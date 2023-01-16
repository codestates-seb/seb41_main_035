import { useRef, useState } from 'react';
import styled from 'styled-components';

const ImageInput = () => {
  // const [imgFile, setImgFile] = useState([]); // 이미지 배열
  // const upload = useRef();

  // const imgUpload = () => {
  //   console.log(upload.current.files);
  //   setImgFile((prev) => [
  //     ...prev,
  //     URL.createObjectURL(upload.current.files[0]),
  //   ]);
  // };
  const [imgFile, setImgFile] = useState([]); // 이미지 배열
  // const upload = useRef();

  const onChangeImg = (e) => {
    const imageLists = e.target.files;
    let imageUrlLists = [...imgFile];

    for (let i = 0; i < imageLists.length; i++) {
      const currentImageUrl = URL.createObjectURL(imageLists[i]);
      imageUrlLists.push(currentImageUrl);
    }
    if (imageUrlLists.length > 2) {
      imageUrlLists = imageUrlLists.slice(0, 2);
    }
    setImgFile(imageUrlLists);
  };

  return (
    <SWrapper>
      <div className="image-upload ">
        <label htmlFor="input-file">
          <div className="btn-upload">업로드</div>
        </label>
        <input
          type="file"
          id="input-file"
          // ref={upload} //참조
          onChange={onChangeImg} // 파일이 추가되면 이벤트가 일어난다.
          multiple // 파일 여러개 선택 가능
          accept="image/*" //모든 이미지 파일형식
        />
      </div>

      <SImagefiles>
        {/* <div style={{ display: 'flex' }}> */}
        <div className="imageadd">
          {/* input에 파일을 넣어줄때마다 state로 값을 배열로 저장해서, 배열의 길이만큼 이미지를 생성 */}
          {imgFile?.map((img, idx) => (
            // <div key={idx} style={{ margin: '20px' }}>
            <div key={idx}>
              <img src={img} alt="img" />
            </div>
          ))}
        </div>
      </SImagefiles>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 15vw;
  .image-upload {
  }
  .image-upload > input {
    display: none;
  }
  //파일 업로드버튼
  .btn-upload {
    width: 3.5vw;
    height: 3vh;
    /* padding: 7px 14px; */
    background-color: #d9d4a6;
    border-radius: 4px;
    color: white;
    font-size: 13px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
`;
const SImagefiles = styled.div`
  .imageadd {
    display: flex;
  }
  img {
    width: 7vw;
    height: 16vh;
    margin: 20px 3px;
  }
`;
export default ImageInput;
